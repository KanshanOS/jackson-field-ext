package io.github.kanshanos.jackson.ext.core.util;

import cn.hutool.json.JSONUtil;
import com.google.common.base.Splitter;
import io.github.kanshanos.jackson.ext.core.annotation.Type;
import io.github.kanshanos.jackson.ext.core.properties.JacksonFieldExtProperties;


import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 解析工具类
 *
 * @author Neo
 * @since 2025/3/26 14:55
 */
public class ParseUtils {
    /**
     * 解析字符串数组类型
     */
    public static List<String> parseStringArray(Object fieldValue, Type type, JacksonFieldExtProperties properties) {
        if (!(fieldValue instanceof String)) {
            throw new IllegalArgumentException(
                    "Expected String for STRING_ARRAY, but got: " + fieldValue.getClass().getName()
            );
        }
        String separator = TypeUtils.separator(type, properties);
        return Splitter.on(separator).trimResults().splitToList((String) fieldValue);
    }

    /**
     * 解析 JSON 数组类型
     */
    public static List<String> parseJsonArray(Object fieldValue) {
        if (!(fieldValue instanceof String)) {
            throw new IllegalArgumentException(
                    "Expected String for JSON_ARRAY, but got: " + fieldValue.getClass().getName()
            );
        }
        try {
            return JSONUtil.toList((String) fieldValue, String.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse JSON array: " + fieldValue, e);
        }
    }

    /**
     * 解析列表类型
     */
    public static List<String> parseList(Object fieldValue) {
        if (!(fieldValue instanceof List)) {
            throw new IllegalArgumentException(
                    "Expected List for LIST, but got: " +
                            (fieldValue == null ? "null" : fieldValue.getClass().getName())
            );
        }

        @SuppressWarnings("unchecked")
        List<Object> list = (List<Object>) fieldValue;
        return list.stream()
                .map(obj -> Objects.toString(obj, ""))
                .collect(Collectors.toList());
    }
}
