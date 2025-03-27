package com.kanshan.jackson.ext.core.util;

import com.fasterxml.jackson.databind.BeanProperty;

import com.kanshan.jackson.ext.core.annotation.Mapping;
import com.kanshan.jackson.ext.core.properties.JacksonFieldExtProperties;
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

    public static String ext(Mapping mapping, JacksonFieldExtProperties properties, BeanProperty property) {
        return StringUtils.isBlank(mapping.ext()) ? property.getName() + properties.getExt_suffix() : mapping.ext();
    }
}
