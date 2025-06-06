package io.github.kanshanos.jackson.ext.core.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.kanshanos.jackson.ext.core.enums.ExceptionStrategy;
import io.github.kanshanos.jackson.ext.core.enums.TrueFalse;
import io.github.kanshanos.jackson.ext.core.handler.AssembleFunctionHandler;


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
     * 扩展字段，override = true 时生效，默认为原始属性名 + Text
     *
     * @return field name
     */
    String ext() default "";

    /**
     * 使用覆盖当前字段，默认为 DEFAULT
     *
     * @return clazz
     */
    TrueFalse override() default TrueFalse.DEFAULT;

    /**
     * 扩展字段值类型
     *
     * @return clazz
     */
    Class<? extends Function<?, ?>> function();

    /**
     * 异常处理策略，默认为 DEFAULT
     *
     * @return strategy
     */
    ExceptionStrategy exception() default ExceptionStrategy.DEFAULT;
}
