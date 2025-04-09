## jackson-field-ext
## 介绍
jackson-field-ext 是一个基于 Spring Boot 的 JSON 数据组装组件，通过注解的方式，实现 JSON 数据的组装，支持枚举类、SpEL、自定义函数等。
## 快速开始
### Maven 依赖
- jdk 17
```xml
<dependency>
    <groupId>io.github.kanshanos</groupId>
    <artifactId>jackson-field-ext-springboot-starter</artifactId>
    <version>{latest-2.x}</version>
</dependency>
```
- jdk 8
```xml
<dependency>
    <groupId>io.github.kanshanos</groupId>
    <artifactId>jackson-field-ext-springboot-starter</artifactId>
    <version>{latest-1.x}</version>
</dependency>
```

## 使用示例

### 示例枚举类
```java
@Getter
public enum CategoryEnum {
    手机(1, "手机", "旧手机"),
    电脑(2, "电脑", "烂电脑"),
    手表(3, "手表", "富玩手表"),
    平板(4, "平板", "屌丝平板"),
    ;

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
### 实体类
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User implements Serializable {
    @AssembleEnum(enumClass = CategoryEnum.class)
    private Integer category;

    @AssembleEnum(enumClass = CategoryEnum.class, type = AssembleType.MANY_TO_MANY)
    private String category1;

    @AssembleEnum(enumClass = CategoryEnum.class,
            type = AssembleType.MANY_TO_MANY,
            srcType = @Type(dataType = DataType.LIST),
            etxType = @Type(dataType = DataType.MAP),
            mapping = @Mapping(src = "code", ref = "desc"),
            ext = "categoryMapping",
            override = true
    )
    private List<Integer> category2;

    @AssembleSpEL(ext = "ageName", expression = "#value > 18 ? '成年' : '未成年'")
    private int age;

    @AssembleFunction(function = MaskEmailFunction.class, override = true)
    private String email;
}
```

### 数据初始化
```java
@GetMapping("get")
public User get() {
    List<Integer> categoryCodes = Lists.newArrayList(
            CategoryEnum.平板.getCode(),
            CategoryEnum.手机.getCode(),
            CategoryEnum.手表.getCode()
    );
    String categoryCodeStringArray = categoryCodes.stream()
            .map(String::valueOf)
            .collect(Collectors.joining(","));

    return new User().setCategory(1)
            .setCategory1(categoryCodeStringArray)
            .setCategory2(categoryCodes)
            .setAge(19)
            .setEmail("123456@gmail.com");
}
```

### 结果展示
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
  },
  "age": 19,
  "ageName": "成年",
  "email": "12**56@gmail.com"
}
```

## 详细使用说明
### 配置项
```yaml
kanshanos:
  ext-field:
    enabled: true
    separator: ','
    src: 'code'
    ref: 'text'
    ext-suffix: 'Text'
```
- enabled: 组件开关配置，默认为： true
- separator: @AssembleEnum.type = MANY_TO_MANY 且 @Type.dataType = STRING_ARRAY 时，用于分割源数据或组装结果数据，默认为： ,
- src: 枚举类中用于匹配源数据的字段，默认为： code
- ref: 枚举类中用于匹配目标数据的字段，默认为： text
- ext-suffix: 组装结果字段后缀，默认为： Text， 当 ext 为空时，ext 的生成逻辑：{当前字段名} + {ext-suffix}

### @AssembleEnum
适用于使用枚举类进行数据组装的场景
- enumClass: 用于组装结果数据的枚举类
- type: 组装类型，默认为 ONE_TO_ONE
- srcType: 用于标注组装源数据的类型，详见 @Type
- extType: 用于标注组装结果数据的类型，详见 @Type
- ext: 组装结果属性名，默认为： Text， 当 ext 为空时，ext 的生成逻辑：{当前字段名} + {ext-suffix}
- override: 是否覆盖源数据，默认为 false

#### @Type
用于标注枚举类中用于匹配源数据的类型和目标数据的类型
- separator: 枚举值分隔符，组装类型为多对多且 dataType = STRING_ARRAY 时生效，默认：,
- dataType: 组装数据类型，默认为 STRING_ARRAY，可选值： STRING_ARRAY、JSON_ARRAY、LIST、MAP

#### @Mapping
用于标注枚举类中用于匹配目标数据的字段和目标数据的后缀
- src: 枚举类中用于匹配源数据的字段，默认为： code
- ref: 枚举类中用于匹配目标数据的字段，默认为： text

### @AssembleSpEL
适用于使用 SpEL 进行数据组装的场景
- expression: SpEL 表达式，只能用 `#value` 来标注当前值，如：`expression = "#value > 18 ? '成年' : '未成年'"`
- ext: 组装结果属性名，默认为： Text， 当 ext 为空时，ext 的生成逻辑：{当前字段名} + {ext-suffix}
- override: 是否覆盖源数据，默认为 false

### @AssembleFunction
适用于使用自定义函数进行数据组装的场景
- function: 自定义函数的实现类，必须实现 Function 接口，默认为空
- ext: 组装结果属性名，默认为： Text， 当 ext 为空时，ext 的生成逻辑：{当前字段名} + {ext-suffix}
- override: 是否覆盖源数据，默认为 false
> 自定义函数的实现类必须实现 Function 接口，推荐使用 `@Component` 进行注解，但不是必须的， 如：
```java
@Component
public class MaskEmailFunction implements Function<String, String> {
    @Override
    public String apply(String o) {
        return StrUtil.hide(o, 2, StrUtil.indexOf(o, '@') - 2);
    }
}
```
