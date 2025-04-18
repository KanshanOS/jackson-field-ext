package io.github.kanshanos.jackson.ext.core.handler;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.SerializerProvider;

import io.github.kanshanos.jackson.ext.core.annotation.AssembleEnum;
import io.github.kanshanos.jackson.ext.core.annotation.Mapping;
import io.github.kanshanos.jackson.ext.core.annotation.Type;
import io.github.kanshanos.jackson.ext.core.cache.EnumCache;
import io.github.kanshanos.jackson.ext.core.enums.AssembleType;
import io.github.kanshanos.jackson.ext.core.enums.DataType;
import io.github.kanshanos.jackson.ext.core.enums.ExceptionStrategy;
import io.github.kanshanos.jackson.ext.core.enums.TrueFalse;
import io.github.kanshanos.jackson.ext.core.util.MappingUtils;
import io.github.kanshanos.jackson.ext.core.util.ParseUtils;
import io.github.kanshanos.jackson.ext.core.util.TypeUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 处理 AssembleEnum 注解
 *
 * @author Neo
 * @since 2025/3/24 16:32
 */
public class AssembleEnumHandler extends AbstractAssembleHandler<AssembleEnum> {

    @Override
    protected AssembleEnum getAnnotation(BeanProperty property) {
        return property.getAnnotation(AssembleEnum.class);
    }

    @Override
    protected void doSerialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (annotation.mapping() == null) {
            return; // 无映射配置时直接返回
        }
        String extFieldName = resolveExtFieldName(annotation.ext());
        Object extFieldValue = resolveExtFieldValue(value);

        serializeWithOverrideStrategy(value, extFieldName, extFieldValue, gen, serializers);
    }

    @Override
    protected TrueFalse getAnnotationOverrideStrategy() {
        return annotation.override();
    }

    @Override
    protected ExceptionStrategy getAnnotationExceptionStrategy() {
        return annotation.exception();
    }

    /**
     * 解析扩展字段值
     */
    private Object resolveExtFieldValue(Object value) {
        Mapping mapping = annotation.mapping();
        String src = MappingUtils.src(mapping, this.properties);
        String ref = MappingUtils.ref(mapping, this.properties);

        return annotation.type() == AssembleType.ONE_TO_ONE
                ? parseOneToOne(value, src, ref)
                : parseManyToMany(value, src, ref);
    }


    /**
     * 一对一解析
     *
     * @author Neo
     * @since 2025/3/25 16:33
     */
    private Object parseOneToOne(Object srcFieldValue, String src, String ref) {
        Enum<?> constant = EnumCache.enumCache(annotation.enumClass(), src, srcFieldValue);
        return constant == null ? null : ReflectUtil.getFieldValue(constant, ref);
    }


    /**
     * 多对多解析
     *
     * @author Neo
     * @since 2025/3/25 16:33
     */
    private Object parseManyToMany(Object srcFieldValue, String src, String ref) {
        List<String> srcFieldValueList = formatSrcFieldValue(srcFieldValue);
        if (CollectionUtils.isEmpty(srcFieldValueList)) {
            return null;
        }

        Map<Object, Object> extFieldMap = EnumCache.enumMap(annotation.enumClass(), src, ref, srcFieldValueList);

        return formatExtFieldValues(extFieldMap, annotation.etxType());
    }


    /**
     * 解析原始数据值
     *
     * @author Neo
     * @since 2025/3/26 13:56
     */
    private List<String> formatSrcFieldValue(Object srcFieldValue) {
        Type type = annotation.srcType();
        DataType dataType = type.dataType();

        if (srcFieldValue == null) {
            return Collections.emptyList();
        }


        // 根据不同数据类型进行解析
        return switch (dataType) {
            case STRING_ARRAY -> ParseUtils.parseStringArray(srcFieldValue, type, this.properties);
            case JSON_ARRAY -> ParseUtils.parseJsonArray(srcFieldValue);
            case LIST -> ParseUtils.parseList(srcFieldValue);
            default -> Collections.emptyList();
        };
    }

    /**
     * 格式化扩展字段值
     *
     * @author Neo
     * @since 2025/3/26 16:29
     */
    private Object formatExtFieldValues(Map<Object, Object> extFieldMap, Type extType) {
        DataType dataType = extType.dataType();
        return switch (dataType) {
            case STRING_ARRAY -> extFieldMap.values().stream()
                    .map(item -> Objects.toString(item, ""))
                    .collect(Collectors.joining(TypeUtils.separator(extType, properties)));
            case JSON_ARRAY -> JSONUtil.toJsonStr(extFieldMap.values());
            case LIST -> extFieldMap.values();
            default -> extFieldMap;
        };
    }
}
