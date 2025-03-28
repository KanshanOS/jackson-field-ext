package io.github.kanshanos.jackson.ext.core.annotation;

import io.github.kanshanos.jackson.ext.core.enums.DataType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据类型
 * AssembleType=MANY_TO_MANY时生效
 *
 * @author Neo
 * @since 2025/3/26 14:49
 */
@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Type {

    /**
     * 枚举值分隔符，组装类型为多对多且 dataType = STRING_ARRAY 时生效，默认：“,”
     */
    String separator() default "";

    /**
     * 元数据类型，组装类型为多对多时生效，默认：STRING_ARRAY
     */
    DataType dataType() default DataType.STRING_ARRAY;
}
