package io.github.kanshanos.jackson.ext.core.annotation;


import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.kanshanos.jackson.ext.core.enums.AssembleType;
import io.github.kanshanos.jackson.ext.core.handler.AssembleEnumHandler;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 组装枚举类
 *
 * @author Neo
 * @since 2025/3/24 13:39
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@JacksonAnnotationsInside
@JsonSerialize(using = AssembleEnumHandler.class)
public @interface AssembleEnum {


    /**
     * 关联的枚举类
     */
    Class<? extends Enum<?>> enumClass();

    /**
     * 组装类型，默认：ONE_TO_ONE
     */
    AssembleType type() default AssembleType.ONE_TO_ONE;

    /**
     * 数据源类型，AssembleEnum.type() = MANY_TO_MANY 时生效
     */
    Type srcType() default @Type;

    /**
     * 扩展数据类型，AssembleEnum.type() = MANY_TO_MANY 时生效
     */
    Type etxType() default @Type;

    /**
     * 映射关系
     */
    Mapping mapping() default @Mapping;
}
