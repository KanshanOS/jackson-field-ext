package io.github.kanshanos.jackson.ext.core.util;

import io.github.kanshanos.jackson.ext.core.annotation.Mapping;
import io.github.kanshanos.jackson.ext.core.properties.JacksonFieldExtProperties;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Neo
 * @since 2025/3/26 13:27
 */
public class MappingUtils {

    public static String src(Mapping mapping, JacksonFieldExtProperties properties) {
        return StringUtils.isBlank(mapping.src()) ? properties.getSrc() : mapping.src();
    }

    public static String ref(Mapping mapping, JacksonFieldExtProperties properties) {
        return StringUtils.isBlank(mapping.ref()) ? properties.getRef() : mapping.ref();
    }
}
