package io.github.kanshanos.jackson.ext.core.cache;

import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.MapUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 枚举缓存
 *
 * @author Neo
 * @since 2025/3/26 14:00
 */
@UtilityClass
public class EnumCache {

    private static final Table<Class<? extends Enum<?>>, String, Map<Object, Enum<?>>> CACHE = HashBasedTable.create();

    /**
     * 获取枚举值
     *
     * @param enumClass 枚举类
     * @param src       字段名
     * @param srcValue  字段值
     * @return 枚举值
     */
    public static Enum<?> enumCache(Class<? extends Enum<?>> enumClass, String src, Object srcValue) {
        if (enumClass == null || src == null || srcValue == null) {
            return null;
        }

        Map<Object, Enum<?>> srcEnumMap = CACHE.get(enumClass, src);
        if (MapUtils.isNotEmpty(srcEnumMap)) {
            return srcEnumMap.get(String.valueOf(srcValue));
        }

        srcEnumMap = buildFieldCache(enumClass.getEnumConstants(), src);
        CACHE.put(enumClass, src, srcEnumMap);

        return srcEnumMap.get(String.valueOf(srcValue));
    }

    /**
     * 创建枚举映射
     *
     * @param enumClass 枚举类
     * @param src       源字段名
     * @param ref       目标字段名
     * @param srcValues 源字段值列表
     * @return 枚举映射
     */
    public static Map<Object, Object> enumMap(Class<? extends Enum<?>> enumClass, String src, String ref, List<String> srcValues) {
        if (enumClass == null || src == null || ref == null || srcValues == null) {
            return Collections.emptyMap();
        }

        Map<Object, Object> result = new LinkedHashMap<>(srcValues.size());
        for (Object srcValue : srcValues) {
            if (srcValue != null) {
                Enum<?> enumValue = enumCache(enumClass, src, srcValue);
                if (enumValue != null) {
                    result.put(srcValue, ReflectUtil.getFieldValue(enumValue, ref));
                }
            }
        }
        return result;
    }

    /**
     * 构建字段缓存
     *
     * @param constants 枚举常量数组
     * @param fieldName 字段名
     * @return 字段缓存
     */
    private static Map<Object, Enum<?>> buildFieldCache(Enum<?>[] constants, String fieldName) {
        Map<Object, Enum<?>> cache = new HashMap<>(constants.length);
        for (Enum<?> constant : constants) {
            Object fieldValue = ReflectUtil.getFieldValue(constant, fieldName);
            if (fieldValue != null) {
                cache.put(String.valueOf(fieldValue), constant);
            }
        }
        return Collections.unmodifiableMap(cache);
    }
}
