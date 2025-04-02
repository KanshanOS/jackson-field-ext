

### 使用示例
```java
public class User implements Serializable {
    
    @AssembleEnum(enumClass = CategoryEnum.class)
    private Integer category;

    @AssembleEnum(enumClass = CategoryEnum.class,
            type = AssembleType.MANY_TO_MANY,
            etxType = @Type(dataType = DataType.JSON_ARRAY)
    )
    private String category1;

    @AssembleSpEL(ext = "ageName", expression = "#value > 18 ? '成年' : '未成年'")
    private int age;

    @AssembleFunction(function = MaskPhoneFunction.class)
    private String mobile;

    @AssembleFunction(ext = "newEmail", override = true, function = MaskEmailFunction.class)
    private String email;
}
```

### 数据初始化
```java
@GetMapping("get")
public User getUser() {
    String category = "1,2,3";
    new User().setCategory(1)
            .setCategory1(category)
            .setAge(19)
            .setMobile("15912345678")
            .setEmail("123456@gmail.com");
}
```

### 结果展示
```json
{
    "category": 1,
    "categoryText": "手机",
    "category1": "1,2,3",
    "category1Text": "[\"手机\",\"电脑\",\"手表\"]",
    "age": 19,
    "ageName": "成年",
    "mobile": "158****1234",
    "email": "123456@gmail.com",
    "newEmail": "12**56@gmail.com"
}
```

### 快速开始
> Maven 依赖
```xml
<dependency>
    <groupId>io.github.kanshanos</groupId>
    <artifactId>jackson-field-ext-springboot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

