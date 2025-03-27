package com.kanshan.jackson.ext.core.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kanshan.jackson.ext.core.handler.AssembleFunctionHandler;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;

/**
 * 通过函数处理数据
 * 更多的使用场景是在当前字段上进行处理，比如：手机号、邮箱数据脱敏等场景
 *
 * @author Neo
 * @since 2025/3/27 08:42
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@JacksonAnnotationsInside
@JsonSerialize(using = AssembleFunctionHandler.class)
public @interface AssembleFunction {

    /**
     * 扩展字段，useExt = true 时生效，默认为原始属性名 + Text
     *
     * @return field name
     */
    String ext() default "";

    /**
     * 使用拓展字段，默认为 false
     *
     * @return clazz
     */
    boolean useExt() default false;

    /**
     * 扩展字段值类型
     *
     * @return clazz
     */
    Class<? extends Function<Object, Object>> function();
}
