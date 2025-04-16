package io.github.kanshanos.jackson.ext.core.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import io.github.kanshanos.jackson.ext.core.enums.ExceptionStrategy;
import io.github.kanshanos.jackson.ext.core.enums.TrueFalse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 组装策略
 *
 * @author Neo
 * @since 2025/4/16 13:09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@JacksonAnnotationsInside
public @interface AssembleStrategy {

    /**
     * 是否忽略，默认：false
     */
    TrueFalse ignore() default TrueFalse.DEFAULT;

    /**
     * 使用覆盖当前字段，默认为 DEFAULT
     *
     * @return clazz
     */
    TrueFalse override() default TrueFalse.DEFAULT;

    /**
     * 异常处理策略，默认为 DEFAULT
     *
     * @return strategy
     */
    ExceptionStrategy exception() default ExceptionStrategy.DEFAULT;

}
