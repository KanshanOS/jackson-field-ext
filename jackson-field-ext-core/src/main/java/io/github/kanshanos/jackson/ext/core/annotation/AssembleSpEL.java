package io.github.kanshanos.jackson.ext.core.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.kanshanos.jackson.ext.core.handler.AssembleSpELHandler;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 通过 SpEL 表达式组装
 *
 * @author Neo
 * @since 2025/3/26 16:38
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@JacksonAnnotationsInside
@JsonSerialize(using = AssembleSpELHandler.class)
public @interface AssembleSpEL {
    /**
     * 扩展字段，默认为原始属性名 + Text
     *
     * @return field name
     */
    String ext() default "";

    /**
     * 扩展字段值类型，默认为 String
     *
     * @return clazz
     */
    Class<?> clazz() default String.class;

    /**
     * SpEL 表达式，示例：#value > 18 ? '成年' : '未成年'
     *
     * @return expression
     */
    String expression() default "";
}
