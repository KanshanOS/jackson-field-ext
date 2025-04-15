package io.github.kanshanos.jackson.ext.core.properties;

import io.github.kanshanos.jackson.ext.core.enums.ExceptionStrategy;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @author Neo
 * @since 2025/3/27 18:42
 */
@Data
@ConfigurationProperties(prefix = ExtFieldProperties.PREFIX)
public class ExtFieldProperties {
    public static final String PREFIX = "kanshanos.ext-field";
    private boolean enabled = true;

    private ExceptionStrategy exceptionStrategy = ExceptionStrategy.ORIGIN_VALUE;

    private String separator = ",";

    private String src = "code";

    private String ref = "desc";

    private String ext_suffix = "Text";

}
