package io.github.kanshanos.jackson.ext.core.handler;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.kanshanos.jackson.ext.core.annotation.AssembleFunction;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.function.Function;

/**
 * 处理 AssembleFunction 注解
 *
 * @author Neo
 * @since 2025/3/26 16:45
 */
public class AssembleFunctionHandler extends AbstractAssembleHandler<AssembleFunction> {
    @Resource
    private ApplicationContext applicationContext;

    @Override
    protected AssembleFunction getAnnotation(BeanProperty property) {
        return property.getAnnotation(AssembleFunction.class);
    }

    @Override
    protected void doSerialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String extFieldName = resolveExtFieldName(annotation.ext());
        Object extFieldValue = applyFunction(annotation.function(), value);
        serializeWithOverrideCheck(value, extFieldName, extFieldValue, gen, serializers, annotation.override());
    }


    /**
     * 调用函数
     *
     * @author Neo
     * @since 2025/4/2 12:58
     */
    private <T, R> R applyFunction(Class<? extends Function<?, ?>> functionClass, T value) {
        @SuppressWarnings("unchecked")
        Function<T, R> function = (Function<T, R>) applicationContext.getBean(functionClass);
        return function.apply(value);
    }
}
