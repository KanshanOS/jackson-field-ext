package io.github.kanshanos.jackson.ext;

import lombok.Getter;

/**
 * 分类枚举
 *
 * @author Neo
 * @since 2025/3/21 10:25
 */
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


    public Integer getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
