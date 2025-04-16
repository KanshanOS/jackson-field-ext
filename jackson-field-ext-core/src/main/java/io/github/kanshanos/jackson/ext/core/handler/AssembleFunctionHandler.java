package io.github.kanshanos.jackson.ext.core.handler;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.kanshanos.jackson.ext.core.annotation.AssembleFunction;
import io.github.kanshanos.jackson.ext.core.enums.ExceptionStrategy;
import io.github.kanshanos.jackson.ext.core.enums.OverrideStrategy;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 处理 AssembleFunction 注解
 *
 * @author Neo
 * @since 2025/3/26 16:45
 */
public class AssembleFunctionHandler extends AbstractAssembleHandler<AssembleFunction> {

    private static final Map<Class<? extends Function<?, ?>>, Function<?, ?>> FUNCTION_CACHE = new ConcurrentHashMap<>();

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
        serializeWithOverrideStrategy(value, extFieldName, extFieldValue, gen, serializers);
    }

    @Override
    protected OverrideStrategy getAnnotationOverrideStrategy() {
        return annotation.override();
    }

    @Override
    protected ExceptionStrategy getAnnotationExceptionStrategy() {
        return annotation.exception();
    }


    /**
     * 调用函数
     *
     * @author Neo
     * @since 2025/4/2 12:58
     */
    private <T, R> R applyFunction(Class<? extends Function<?, ?>> functionClass, T value) {
        Function<T, R> function = getFunction(functionClass);
        return function.apply(value);
    }


    /**
     * 根据给定的函数类获取函数实例
     * 该方法首先尝试从Spring容器中获取函数实例，如果不可用，则尝试从缓存中获取
     * 如果两者都不可用，则尝试创建一个新的函数实例，并将其添加到缓存中
     *
     * @author Neo
     * @since 2025/4/3 09:34
     */
    @SuppressWarnings("unchecked")
    public <T, R> Function<T, R> getFunction(Class<? extends Function<?, ?>> functionClass) {
        // 优先从 Spring 容器中获取 function
        Function<T, R> function = (Function<T, R>) applicationContext.getBeanProvider(functionClass).getIfAvailable();
        if (Objects.nonNull(function)) {
            return function;
        }

        // 从缓存中获取 function
        function = (Function<T, R>) FUNCTION_CACHE.get(functionClass);
        if (Objects.nonNull(function)) {
            return function;
        }

        try {
            // 创建函数实例
            function = createFunctionInstance(functionClass);
            FUNCTION_CACHE.put(functionClass, function);
            return function;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create function instance: " + functionClass.getName(), e);
        }
    }

    /**
     * 创建函数实例
     *
     * @author Neo
     * @since 2025/4/3 09:34
     */
    @SuppressWarnings("unchecked")
    private <T, R> Function<T, R> createFunctionInstance(Class<? extends Function<?, ?>> functionClass) {
        try {
            Class<? extends Function<Object, Object>> castedClass = (Class<? extends Function<Object, Object>>) functionClass;
            return (Function<T, R>) castedClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate function: " + functionClass.getName(), e);
        }
    }

}
