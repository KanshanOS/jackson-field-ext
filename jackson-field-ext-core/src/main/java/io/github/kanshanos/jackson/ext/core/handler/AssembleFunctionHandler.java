package io.github.kanshanos.jackson.ext.core.handler;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.kanshanos.jackson.ext.core.annotation.AssembleFunction;


import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 处理 AssembleFunction 注解
 *
 * @author Neo
 * @since 2025/3/26 16:45
 */
public class AssembleFunctionHandler extends AbstractAssembleHandler<AssembleFunction> {

    private static final Map<Class<?>, Function<Object, Object>> FUNCTION_CACHE = new ConcurrentHashMap<>();

    @Override
    protected AssembleFunction getAnnotation(BeanProperty property) {
        return property.getAnnotation(AssembleFunction.class);
    }

    @Override
    protected void doSerialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String extFieldName = resolveExtFieldName(annotation.ext());
        Object extFieldValue = applyFunction(value, annotation.function());
        serializeWithOverrideCheck(value, extFieldName, extFieldValue, gen, serializers, annotation.override());
    }

    private Object applyFunction(Object value, Class<? extends Function<Object, Object>> functionClass) throws IOException {
        try {
            Function<Object, Object> function = FUNCTION_CACHE.computeIfAbsent(functionClass, this::createFunctionInstance);
            return function.apply(value);
        } catch (Exception e) {
            throw new IOException("Failed to apply function: " + functionClass.getName(), e);
        }
    }

    private Function<Object, Object> createFunctionInstance(Class<?> functionClass) {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends Function<Object, Object>> castedClass = (Class<? extends Function<Object, Object>>) functionClass;
            return castedClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate function: " + functionClass.getName(), e);
        }
    }
}
