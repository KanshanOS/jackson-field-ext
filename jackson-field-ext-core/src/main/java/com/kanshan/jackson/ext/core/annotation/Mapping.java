package com.kanshan.jackson.ext.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据映射
 *
 * @author Neo
 * @since 2025/3/26 14:49
 */
@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapping {

    /**
     * 要获取的数据源对象属性
     *
     * @return field name
     */
    String src() default "";

    /**
     * 要设置的目标对象属性。
     * 如果为空，则默认为 key 字段。
     *
     * @return field name
     */
    String ref() default "";

    /**
     * 扩展字段，默认为原始属性名 + Text
     *
     * @return field name
     */
    String ext() default "";
}
