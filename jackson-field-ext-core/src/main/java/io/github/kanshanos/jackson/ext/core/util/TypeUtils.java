package io.github.kanshanos.jackson.ext.core.util;


import io.github.kanshanos.jackson.ext.core.annotation.Type;

import io.github.kanshanos.jackson.ext.core.properties.JacksonFieldExtProperties;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Neo
 * @since 2025/3/26 15:05
 */
public class TypeUtils {

    public static String separator(Type type, JacksonFieldExtProperties properties) {
        return StringUtils.defaultIfBlank(type.separator(), properties.getSeparator());
    }
}
