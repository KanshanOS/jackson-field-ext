package io.github.kanshanos.jackson.ext.core.util;

import com.fasterxml.jackson.databind.BeanProperty;

import io.github.kanshanos.jackson.ext.core.annotation.Mapping;
import io.github.kanshanos.jackson.ext.core.properties.JacksonFieldExtProperties;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Neo
 * @since 2025/3/26 13:27
 */
public class MappingUtils {

    public static String src(Mapping mapping, JacksonFieldExtProperties properties) {
        return StringUtils.defaultIfBlank(mapping.src(), properties.getSrc());
    }

    public static String ref(Mapping mapping, JacksonFieldExtProperties properties) {
        return StringUtils.defaultIfBlank(mapping.ref(), properties.getRef());
    }

    public static String ext(Mapping mapping, JacksonFieldExtProperties properties, BeanProperty property) {
        return StringUtils.defaultIfBlank(mapping.ext(), property.getName() + properties.getExt_suffix());
    }
}
