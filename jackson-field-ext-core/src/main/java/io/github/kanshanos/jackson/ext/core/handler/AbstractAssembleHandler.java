package io.github.kanshanos.jackson.ext.core.handler;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import io.github.kanshanos.jackson.ext.core.context.AssembleContext;
import io.github.kanshanos.jackson.ext.core.enums.ExceptionStrategy;
import io.github.kanshanos.jackson.ext.core.enums.TrueFalse;
import io.github.kanshanos.jackson.ext.core.log.ILog;
import io.github.kanshanos.jackson.ext.core.properties.ExtFieldProperties;
import org.apache.commons.lang3.StringUtils;

import jakarta.annotation.Resource;
import java.io.IOException;

/**
 * 抽象的 Assemble 处理类，提取公共逻辑
 */
public abstract class AbstractAssembleHandler<T> extends JsonSerializer<Object> implements ContextualSerializer {

    @Resource
    protected ExtFieldProperties properties;

    @Resource
    private ILog log;

    protected T annotation;

    protected BeanProperty property;

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // 判断是否开启扩展字段功能
        if (!properties.isEnabled() || annotation == null) {
            serializers.defaultSerializeValue(value, gen);
            return;
        }

        // 判断当前请求是否忽略
        if (TrueFalse.TRUE == AssembleContext.ignore()) {
            serializers.defaultSerializeValue(value, gen);
            return;
        }

        // 模板方法：子类实现具体的序列化逻辑
        try {
            doSerialize(value, gen, serializers);
        } catch (Exception e) {
            ExceptionStrategy exceptionStrategy = this.getExceptionStrategy();
            switch (exceptionStrategy) {
                case THROWS:
                    throw e;
                case THROWS_AND_LOG:
                    log.error("JacksonFieldExtException", e);
                    throw e;
                case ORIGIN_VALUE:
                    serializers.defaultSerializeValue(value, gen);
                    break;
                case ORIGIN_AND_LOG:
                    serializers.defaultSerializeValue(value, gen);
                    log.error("JacksonFieldExtException", e);
                    break;
                case HIDDEN_VALUE:
                    serializers.defaultSerializeValue(StringUtils.EMPTY, gen);
                    break;
                case HIDDEN_AND_LOG:
                    serializers.defaultSerializeValue(StringUtils.EMPTY, gen);
                    log.error("JacksonFieldExtException", e);
                    break;
            }
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        this.annotation = getAnnotation(property);
        this.property = property;
        return this;
    }

    /**
     * 获取注解，由子类实现
     */
    protected abstract T getAnnotation(BeanProperty property);

    /**
     * 执行具体的序列化逻辑，由子类实现
     */
    protected abstract void doSerialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException;

    /**
     * 获取注解覆盖策略策略
     */
    protected abstract TrueFalse getAnnotationOverrideStrategy();

    /**
     * 获取注解异常策略
     */
    protected abstract ExceptionStrategy getAnnotationExceptionStrategy();

    /**
     * 获取覆盖策略
     */
    protected TrueFalse getOverrideStrategy() {
        TrueFalse override = AssembleContext.override();
        if (TrueFalse.DEFAULT != override) {
            return override;
        }
        return TrueFalse.DEFAULT == getAnnotationOverrideStrategy()
                ? properties.getOverride() : getAnnotationOverrideStrategy();
    }

    /**
     * 获取异常策略
     */
    protected ExceptionStrategy getExceptionStrategy() {
        ExceptionStrategy exception = AssembleContext.exception();
        if (ExceptionStrategy.DEFAULT != exception) {
            return exception;
        }
        return ExceptionStrategy.DEFAULT == getAnnotationExceptionStrategy()
                ? properties.getException() : getAnnotationExceptionStrategy();
    }


    /**
     * 解析扩展字段名
     */
    protected String resolveExtFieldName(String ext) {
        return StringUtils.defaultIfBlank(ext, property.getName() + properties.getExt_suffix());
    }

    /**
     * 序列化带有扩展字段的逻辑
     */
    protected void serializeWithExtField(Object originalValue, Object extFieldValue, String extFieldName,
                                         JsonGenerator gen, SerializerProvider serializers) throws IOException {
        serializers.defaultSerializeValue(originalValue, gen);
        gen.writeObjectField(extFieldName, extFieldValue);
    }

    /**
     * 根据是否允许覆盖现有值来序列化对象
     */
    protected void serializeWithOverrideStrategy(Object value, String extFieldName, Object extFieldValue,
                                                 JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (TrueFalse.TRUE == this.getOverrideStrategy()) {
            serializers.defaultSerializeValue(extFieldValue, gen);
        } else {
            serializeWithExtField(value, extFieldValue, extFieldName, gen, serializers);
        }
    }
}
