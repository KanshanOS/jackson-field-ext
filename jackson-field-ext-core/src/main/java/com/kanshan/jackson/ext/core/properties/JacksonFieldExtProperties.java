package com.kanshan.jackson.ext.core.properties;

import com.kanshan.jackson.ext.core.constant.ExtFieldConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @author Neo
 * @since 2025/3/27 18:42
 */
@Data
@ConfigurationProperties(prefix = ExtFieldConstants.PROPERTIES_PREFIX)
public class JacksonFieldExtProperties {
    private String separator = ",";

    private String src = "code";

    private String ref = "desc";

    private String ext_suffix = "Text";

}
