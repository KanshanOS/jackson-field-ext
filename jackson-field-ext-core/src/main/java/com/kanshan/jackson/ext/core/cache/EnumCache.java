package com.kanshan.jackson.ext.core.cache;

import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Neo
 * @since 2025/3/26 14:00
 */
public class EnumCache {
    private static final Table<Class<? extends Enum<?>>, String, Map<Object, Enum<?>>> CACHE = HashBasedTable.create();

    public static Enum<?> enumCache(Class<? extends Enum<?>> enumClass, String src, Object srcValue) {
        Map<Object, Enum<?>> srcEnumMap = CACHE.get(enumClass, src);
        if (MapUtils.isNotEmpty(srcEnumMap)) {
            return srcEnumMap.get(String.valueOf(srcValue));
        }


        Enum<?>[] constants = enumClass.getEnumConstants();
        srcEnumMap = new HashMap<>(constants.length);

        for (Enum<?> constant : constants) {
            srcEnumMap.put(String.valueOf(ReflectUtil.getFieldValue(constant, src)), constant);
        }

        CACHE.put(enumClass, src, srcEnumMap);

        return srcEnumMap.get(String.valueOf(srcValue));
    }
}
