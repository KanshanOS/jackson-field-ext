package io.github.kanshanos.jackson.ext.core.util;

import io.github.kanshanos.jackson.ext.core.annotation.Mapping;
import io.github.kanshanos.jackson.ext.core.properties.ExtFieldProperties;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Neo
 * @since 2025/3/26 13:27
 */
public class MappingUtils {

    public static String src(Mapping mapping, ExtFieldProperties properties) {
        return StringUtils.defaultIfBlank(mapping.src(), properties.getSrc());
    }

    public static String ref(Mapping mapping, ExtFieldProperties properties) {
        return StringUtils.defaultIfBlank(mapping.ref(), properties.getRef());
    }
}
