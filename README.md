# jackson-field-ext

## Introduction

`jackson-field-ext` is a Spring Boot-based JSON data assembly component that enhances Jackson's serialization capabilities through annotations. It provides flexible mechanisms to enrich JSON output with additional fields, supporting features like enum mapping, SpEL expressions, and custom function processing. The project is designed to simplify data transformation and desensitization tasks, such as masking sensitive information (e.g., phone numbers, emails, or IDs).

The project consists of three modules:

- **jackson-field-ext-core**: Core functionality, including annotations, handlers, and utilities.
- **jackson-field-ext-springboot-starter**: Spring Boot auto-configuration for seamless integration.
- **jackson-field-ext-sample**: A sample application demonstrating usage.

## Features

- **Enum Mapping**: Map enum values to descriptive fields (e.g., mapping a code to its description) with support for one-to-one and many-to-many relationships.
- **SpEL Expressions**: Use Spring Expression Language to dynamically compute field values based on existing data.
- **Custom Functions**: Apply custom transformations (e.g., data desensitization) using Java `Function` implementations.
- **Data Desensitization**: Built-in functions for masking sensitive data like addresses, bank cards, emails, phone numbers, IDs, and names.
- **Flexible Configuration**: Customize behavior via properties and annotations, including field naming, exception handling, and override strategies.
- **Exception Handling**: Configurable strategies for handling errors during serialization (e.g., throw, log, return original value, or hide).
- **Interceptor Support**: Control assembly behavior per request using the `@AssembleStrategy` annotation.
- **Spring Boot Integration**: Auto-configuration and property support for easy setup.

## Quick Start

### Prerequisites

- Java 8 or 17
- Maven 3.6+
- Spring Boot 2.0.9.RELEASE or 3.4.4

### Maven Dependency

Add the Spring Boot starter dependency to your `pom.xml`:

For **JDK 8**:

```xml
<dependency>
    <groupId>io.github.kanshanos</groupId>
    <artifactId>jackson-field-ext-springboot-starter</artifactId>
    <version>{latest-1.x}</version>
</dependency>
```

For **JDK 17**:

```xml
<dependency>
    <groupId>io.github.kanshanos</groupId>
    <artifactId>jackson-field-ext-springboot-starter</artifactId>
    <version>{latest-2.x}</version>
</dependency>
```

### Configuration

Add the following to your `application.yml` to customize the component (optional, defaults are provided):

```yaml
kanshanos:
  ext-field:
    enabled: true
    separator: ','
    src: 'code'
    ref: 'desc'
    ext-suffix: 'Text'
    override: false
    exception: ORIGIN_VALUE
```

- `enabled`: Enable or disable the component (default: `true`).
- `separator`: Separator for `STRING_ARRAY` data in many-to-many enum mappings (default: `,`).
- `src`: Enum field for source data matching (default: `code`).
- `ref`: Enum field for result data (default: `desc`).
- `ext-suffix`: Suffix for generated extension fields when `ext` is not specified (default: `Text`).
- `override`: Default override behavior for fields (default: `false`).
- `exception`: Default exception handling strategy (default: `ORIGIN_VALUE`).

## Usage Examples

### 1. Enum Mapping

Define an enum:

```java
import lombok.Getter;

@Getter
public enum CategoryEnum {
    手机(1, "手机", "旧手机"),
    电脑(2, "电脑", "烂电脑"),
    手表(3, "手表", "富玩手表"),
    平板(4, "平板", "屌丝平板");

    private final Integer code;
    private final String desc;
    private final String alias;

    CategoryEnum(Integer code, String desc, String alias) {
        this.code = code;
        this.desc = desc;
        this.alias = alias;
    }
}
```

Use `@AssembleEnum` in a DTO:

```java
import io.github.kanshanos.jackson.ext.core.annotation.*;
import io.github.kanshanos.jackson.ext.core.enums.*;
import lombok.Data;

@Data
public class User {
    @AssembleEnum(enumClass = CategoryEnum.class, mapping = @Mapping(src = "code", ref = "desc"))
    private Integer category;

    @AssembleEnum(enumClass = CategoryEnum.class, type = AssembleType.MANY_TO_MANY, 
                  mapping = @Mapping(src = "code", ref = "desc"))
    private String category1;

    @AssembleEnum(enumClass = CategoryEnum.class, type = AssembleType.MANY_TO_MANY,
                  srcType = @Type(dataType = DataType.LIST),
                  etxType = @Type(dataType = DataType.MAP),
                  mapping = @Mapping(src = "code", ref = "desc"),
                  ext = "categoryMapping", override = TrueFalse.TRUE)
    private List<Integer> category2;
}
```

Controller:

```java
import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/user")
    public User getUser() {
        User user = new User();
        user.setCategory(1);
        user.setCategory1("4,1,3");
        user.setCategory2(Lists.newArrayList(4, 1, 3));
        return user;
    }
}
```

**Output**:

```json
{
  "category": 1,
  "categoryText": "手机",
  "category1": "4,1,3",
  "category1Text": "平板,手机,手表",
  "category2": {
    "4": "平板",
    "1": "手机",
    "3": "手表"
  }
}
```

### 2. SpEL Expressions

Add a field with a SpEL expression:

```java
@Data
public class User {
    @AssembleSpEL(ext = "ageName", expression = "#value > 18 ? '成年' : '未成年'")
    private int age;
}
```

**Output** (with `age = 19`):

```json
{
  "age": 19,
  "ageName": "成年"
}
```

### 3. Custom Functions (Data Desensitization)

Use built-in desensitization functions:

```java
@Data
public class User {
    @AssembleFunction(function = {MaskEmailFunction.class}, override = TrueFalse.TRUE)
    private String email;

    @AssembleFunction(function = {MaskMobilePhoneFunction.class})
    private String phone;
}
```

**Output** (with `email = "123456@gmail.com"`, `phone = "13512345678"`):

```json
{
  "email": "12**56@gmail.com",
  "phone": "135****5678",
  "phoneText": "135****5678"
}
```

Define a custom function:

```java
import cn.hutool.crypto.digest.MD5;
import org.springframework.stereotype.Component;
import java.util.function.Function;

@Component
public class MD5Function implements Function<String, String> {
    @Override
    public String apply(String input) {
        return MD5.create().digestHex(input);
    }
}
```

Use it:

```java
@Data
public class User {
    @AssembleFunction(function = {MaskEmailFunction.class, MD5Function.class}, override = TrueFalse.TRUE)
    private String email;
}
```

**Output** (with `email = "123456@gmail.com"`):

```json
{
  "email": "<MD5 hash of 12**56@gmail.com>"
}
```

### 4. Controlling Assembly with `@AssembleStrategy`

Apply per-request assembly strategies:

```java
@RestController
@RequestMapping("user")
public class UserController {
    @AssembleStrategy(ignore = TrueFalse.FALSE, override = TrueFalse.TRUE, exception = ExceptionStrategy.ORIGIN_VALUE)
    @GetMapping("strategy")
    public User strategy() {
        return new User().setCategory(1).setEmail("123456@gmail.com").setAge(19);
    }
}
```

This ensures the endpoint respects the specified override and exception strategies.

## Detailed Usage

### Annotations

#### `@AssembleEnum`

- **Purpose**: Map enum values to descriptive fields.
- **Attributes**:
    - `enumClass`: The enum class to use.
    - `type`: `ONE_TO_ONE` or `MANY_TO_MANY` (default: `ONE_TO_ONE`).
    - `srcType`/`etxType`: Define source/result data types via `@Type`.
    - `mapping`: Define source (`src`) and result (`ref`) fields via `@Mapping`.
    - `ext`: Extension field name (default: `<fieldName> + ext-suffix`).
    - `override`: Override original field (default: `False`).
    - `exception`: Exception handling strategy (default: `DEFAULT`).

#### `@AssembleSpEL`

- **Purpose**: Compute field values using SpEL.
- **Attributes**:
    - `expression`: SpEL expression using `#value` for the current field value.
    - `ext`: Extension field name.
    - `override`: Override original field.
    - `exception`: Exception handling strategy.

#### `@AssembleFunction`

- **Purpose**: Apply custom transformations via `Function` implementations.
- **Attributes**:
    - `function`: Array of `Function` classes to apply sequentially.
    - `ext`: Extension field name.
    - `override`: Override original field.
    - `exception`: Exception handling strategy.

#### `@AssembleStrategy`

- **Purpose**: Control assembly behavior for a controller or method.
- **Attributes**:
    - `ignore`: Skip assembly (default: `False`).
    - `override`: Override fields by default.
    - `exception`: Exception handling strategy.

#### `@Type`

- **Purpose**: Define data types for many-to-many enum mappings.
- **Attributes**:
    - `dataType`: `STRING_ARRAY`, `JSON_ARRAY`, `LIST`, or `MAP` (default: `STRING_ARRAY`).
    - `separator`: Separator for `STRING_ARRAY` (default: `,`).

#### `@Mapping`

- **Purpose**: Define source and result fields for enum mappings.
- **Attributes**:
    - `src`: Source field in enum (default: `code`).
    - `ref`: Result field in enum (default: `desc`).

### Built-in Desensitization Functions

The following functions are provided for sensitive data masking:

- `MaskAddressFunction`: Masks address details (e.g., "上海市浦东新区\*\*\*\*\*\*").
- `MaskBankCardFunction`: Masks bank card numbers (e.g., "1234 \*\*\*\* \*\*\*\* \*\*\*\* 6789").
- `MaskCarLicenseFunction`: Masks car license plates (e.g., "苏D4\*\*\*0").
- `MaskEmailFunction`: Masks email addresses (e.g., "12\*\*56@gmail.com").
- `MaskFixedPhoneFunction`: Masks fixed phone numbers.
- `MaskIdcardFunction`: Masks ID card numbers (e.g., "110101\*\*\*\*\*\*\*\*1234").
- `MaskMobilePhoneFunction`: Masks mobile phone numbers (e.g., "135\*\*\*\*5678").
- `MaskNameFunction`: Masks names, preserving surname (e.g., "诸葛\*\*").

### Exception Handling Strategies

- `DEFAULT`: Use global default (set via properties).
- `THROWS`: Throw the exception.
- `THROWS_AND_LOG`: Log and throw the exception.
- `ORIGIN_VALUE`: Return the original value.
- `ORIGIN_AND_LOG`: Log and return the original value.
- `HIDDEN_VALUE`: Return an empty string.
- `HIDDEN_AND_LOG`: Log and return an empty string.

## Building and Running the Sample

1. Clone the repository:

   ```bash
   git clone https://github.com/KanshanOS/jackson-field-ext.git
   ```

2. Build the project:

   ```bash
   cd jackson-field-ext
   mvn clean install
   ```

3. Run the sample application:

   ```bash
   cd jackson-field-ext-sample
   mvn spring-boot:run
   ```

4. Access the sample endpoint:

   ```bash
   curl http://localhost:8080/user/get
   ```

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/YourFeature`).
3. Commit your changes (`git commit -m 'Add YourFeature'`).
4. Push to the branch (`git push origin feature/YourFeature`).
5. Open a pull request.

## License

This project is licensed under the Apache License 2.0.

## Contact

- **Author**: Kanshan
- **Email**: im.neoyu@gmail.com
- **GitHub**: https://github.com/KanshanOS
- **Project URL**: https://github.com/KanshanOS/jackson-field-ext
