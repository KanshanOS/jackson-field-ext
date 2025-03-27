package com.kanshan.jackson.ext.core.handler;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.kanshan.jackson.ext.core.properties.JacksonFieldExtProperties;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 抽象的 Assemble 处理类，提取公共逻辑
 */
public abstract class AbstractAssembleHandler<T> extends JsonSerializer<Object> implements ContextualSerializer {

    @Resource
    protected JacksonFieldExtProperties properties;

    protected T annotation;

    protected BeanProperty property;

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (annotation == null) {
            serializers.defaultSerializeValue(value, gen);
            return;
        }

        // 模板方法：子类实现具体的序列化逻辑
        doSerialize(value, gen, serializers);
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
     * 解析扩展字段名
     */
    protected String resolveExtFieldName(String extAnnotation) {
        return StringUtils.isBlank(extAnnotation)
                ? property.getName() + properties.getExt_suffix()
                : extAnnotation;
    }

    /**
     * 序列化带有扩展字段的逻辑
     */
    protected void serializeWithExtField(Object originalValue, Object extFieldValue, String extFieldName,
                                         JsonGenerator gen, SerializerProvider serializers) throws IOException {
        serializers.defaultSerializeValue(originalValue, gen);
        gen.writeObjectField(extFieldName, extFieldValue);
    }
}
