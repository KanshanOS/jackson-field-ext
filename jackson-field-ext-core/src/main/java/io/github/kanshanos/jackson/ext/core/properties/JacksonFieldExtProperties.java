package io.github.kanshanos.jackson.ext.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @author Neo
 * @since 2025/3/27 18:42
 */
@Data
@ConfigurationProperties(prefix = "ext.field")
public class JacksonFieldExtProperties {
    private boolean enabled = true;

    private String separator = ",";

    private String src = "code";

    private String ref = "desc";

    private String ext_suffix = "Text";

}
