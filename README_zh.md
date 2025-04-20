# jackson-field-ext

## 简介

`jackson-field-ext` 是一个基于 Spring Boot 的 JSON 数据组装组件，通过注解增强 Jackson 的序列化功能。它提供了灵活的机制，通过添加额外字段丰富 JSON 输出，支持枚举映射、SpEL 表达式和自定义函数处理等功能。该项目旨在简化数据转换和敏感信息脱敏任务，例如屏蔽电话号码、邮箱或身份证号等敏感数据。

项目包含三个模块：

- **jackson-field-ext-core**：核心功能，包括注解、处理器和工具类。
- **jackson-field-ext-springboot-starter**：Spring Boot 自动配置，方便集成。
- **jackson-field-ext-sample**：示例应用，展示使用方法。

## 功能

- **枚举映射**：将枚举值映射为描述性字段（例如，将代码映射为描述），支持一对一和多对多关系。
- **SpEL 表达式**：使用 Spring 表达式语言根据现有数据动态计算字段值。
- **自定义函数**：通过 Java `Function` 实现应用自定义转换（例如，数据脱敏）。
- **数据脱敏**：内置函数用于屏蔽敏感数据，例如地址、银行卡、邮箱、电话号码、身份证号和姓名。
- **灵活配置**：通过属性和注解自定义行为，包括字段命名、异常处理和覆盖策略。
- **异常处理**：可配置的序列化错误处理策略（例如，抛出、记录、返回原始值或隐藏）。
- **拦截器支持**：使用 `@AssembleStrategy` 注解按请求控制组装行为。
- **Spring Boot 集成**：自动配置和属性支持，易于设置。

## 快速开始

### 前提条件

- Java 8 或 17
- Maven 3.6+
- Spring Boot 2.0.9.RELEASE 或 3.4.4

### Maven 依赖

在 `pom.xml` 中添加 Spring Boot starter 依赖：

对于 **JDK 8**：

```xml
<dependency>
    <groupId>io.github.kanshanos</groupId>
    <artifactId>jackson-field-ext-springboot-starter</artifactId>
    <version>{latest-1.x}</version>
</dependency>
```

对于 **JDK 17**：

```xml
<dependency>
    <groupId>io.github.kanshanos</groupId>
    <artifactId>jackson-field-ext-springboot-starter</artifactId>
    <version>{latest-2.x}</version>
</dependency>
```

### 配置

在 `application.yml` 中添加以下内容以自定义组件（可选，提供默认值）：

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

- `enabled`：启用或禁用组件（默认：`true`）。
- `separator`：多对多枚举映射中 `STRING_ARRAY` 数据的分隔符（默认：`,`）。
- `src`：枚举中用于匹配源数据的字段（默认：`code`）。
- `ref`：枚举中用于结果数据的字段（默认：`desc`）。
- `ext-suffix`：未指定 `ext` 时生成的扩展字段后缀（默认：`Text`）。
- `override`：字段的默认覆盖行为（默认：`false`）。
- `exception`：默认异常处理策略（默认：`ORIGIN_VALUE`）。

## 使用示例

### 1. 枚举映射

定义枚举：

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

在 DTO 中使用 `@AssembleEnum`：

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

控制器：

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

**输出**：

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

### 2. SpEL 表达式

添加带有 SpEL 表达式的字段：

```java
@Data
public class User {
    @AssembleSpEL(ext = "ageName", expression = "#value > 18 ? '成年' : '未成年'")
    private int age;
}
```

**输出**（`age = 19`）：

```json
{
  "age": 19,
  "ageName": "成年"
}
```

### 3. 自定义函数（数据脱敏）

使用内置脱敏函数：

```java
@Data
public class User {
    @AssembleFunction(function = {MaskEmailFunction.class}, override = TrueFalse.TRUE)
    private String email;

    @AssembleFunction(function = {MaskMobilePhoneFunction.class})
    private String phone;
}
```

**输出**（`email = "123456@gmail.com"`, `phone = "13512345678"`）：

```json
{
  "email": "12**56@gmail.com",
  "phone": "135****5678",
  "phoneText": "135****5678"
}
```

定义自定义函数：

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

使用它：

```java
@Data
public class User {
    @AssembleFunction(function = {MaskEmailFunction.class, MD5Function.class}, override = TrueFalse.TRUE)
    private String email;
}
```

**输出**（`email = "123456@gmail.com"`）：

```json
{
  "email": "<12**56@gmail.com 的 MD5 哈希值>"
}
```

### 4. 使用 `@AssembleStrategy` 控制组装

按请求应用组装策略：

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

这确保该端点遵循指定的覆盖和异常处理策略。

## 详细使用说明

### 注解

#### `@AssembleEnum`

- **用途**：将枚举值映射为描述性字段。
- **属性**：
    - `enumClass`：使用的枚举类。
    - `type`：`ONE_TO_ONE` 或 `MANY_TO_MANY`（默认：`ONE_TO_ONE`）。
    - `srcType`/`etxType`：通过 `@Type` 定义源/结果数据类型。
    - `mapping`：通过 `@Mapping` 定义源（`src`）和结果（`ref`）字段。
    - `ext`：扩展字段名称（默认：`<字段名> + ext-suffix`）。
    - `override`：是否覆盖原始字段（默认：`False`）。
    - `exception`：异常处理策略（默认：`DEFAULT`）。

#### `@AssembleSpEL`

- **用途**：使用 SpEL 计算字段值。
- **属性**：
    - `expression`：SpEL 表达式，使用 `#value` 表示当前字段值。
    - `ext`：扩展字段名称。
    - `override`：是否覆盖原始字段。
    - `exception`：异常处理策略。

#### `@AssembleFunction`

- **用途**：通过 `Function` 实现应用自定义转换。
- **属性**：
    - `function`：按顺序应用的 `Function` 类数组。
    - `ext`：扩展字段名称。
    - `override`：是否覆盖原始字段。
    - `exception`：异常处理策略。

#### `@AssembleStrategy`

- **用途**：控制控制器或方法的组装行为。
- **属性**：
    - `ignore`：是否跳过组装（默认：`False`）。
    - `override`：默认覆盖字段。
    - `exception`：异常处理策略。

#### `@Type`

- **用途**：定义多对多枚举映射的数据类型。
- **属性**：
    - `dataType`：`STRING_ARRAY`、`JSON_ARRAY`、`LIST` 或 `MAP`（默认：`STRING_ARRAY`）。
    - `separator`：`STRING_ARRAY` 的分隔符（默认：`,`）。

#### `@Mapping`

- **用途**：定义枚举映射的源和结果字段。
- **属性**：
    - `src`：枚举中的源字段（默认：`code`）。
    - `ref`：枚举中的结果字段（默认：`desc`）。

### 内置脱敏函数

提供以下函数用于屏蔽敏感数据：

- `MaskAddressFunction`：屏蔽地址详情（例如，"上海市浦东新区\*\*\*\*\*\*"）。
- `MaskBankCardFunction`：屏蔽银行卡号（例如，"1234 \*\*\*\* \*\*\*\* \*\*\*\* 6789"）。
- `MaskCarLicenseFunction`：屏蔽车牌号（例如，"苏D4\*\*\*0"）。
- `MaskEmailFunction`：屏蔽邮箱地址（例如，"12\*\*56@gmail.com"）。
- `MaskFixedPhoneFunction`：屏蔽固定电话号码。
- `MaskIdcardFunction`：屏蔽身份证号（例如，"110101\*\*\*\*\*\*\*\*1234"）。
- `MaskMobilePhoneFunction`：屏蔽手机号码（例如，"135\*\*\*\*5678"）。
- `MaskNameFunction`：屏蔽姓名，保留姓氏（例如，"诸葛\*\*"）。

### 异常处理策略

- `DEFAULT`：使用全局默认值（通过属性设置）。
- `THROWS`：抛出异常。
- `THROWS_AND_LOG`：记录并抛出异常。
- `ORIGIN_VALUE`：返回原始值。
- `ORIGIN_AND_LOG`：记录并返回原始值。
- `HIDDEN_VALUE`：返回空字符串。
- `HIDDEN_AND_LOG`：记录并返回空字符串。

## 构建和运行示例

1. 克隆仓库：

   ```bash
   git clone https://github.com/KanshanOS/jackson-field-ext.git
   ```

2. 构建项目：

   ```bash
   cd jackson-field-ext
   mvn clean install
   ```

3. 运行示例应用：

   ```bash
   cd jackson-field-ext-sample
   mvn spring-boot:run
   ```

4. 访问示例端点：

   ```bash
   curl http://localhost:8080/user/get
   ```

## 贡献

欢迎贡献！请按照以下步骤操作：

1. Fork 仓库。
2. 创建功能分支（`git checkout -b feature/YourFeature`）。
3. 提交更改（`git commit -m 'Add YourFeature'`）。
4. 推送分支（`git push origin feature/YourFeature`）。
5. 提交 Pull Request。

## 许可证

本项目采用 Apache License 2.0 许可。

## 联系方式

- **作者**：Kanshan
- **邮箱**：im.neoyu@gmail.com
- **GitHub**：https://github.com/KanshanOS
- **项目地址**：https://github.com/KanshanOS/jackson-field-ext
