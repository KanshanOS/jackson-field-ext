package io.github.kanshanos.jackson.ext.config;

import io.github.kanshanos.jackson.ext.core.handler.AssembleEnumHandler;
import io.github.kanshanos.jackson.ext.core.handler.AssembleFunctionHandler;
import io.github.kanshanos.jackson.ext.core.handler.AssembleSpELHandler;
import io.github.kanshanos.jackson.ext.core.log.ILog;
import io.github.kanshanos.jackson.ext.core.log.Slf4jLog;
import io.github.kanshanos.jackson.ext.core.properties.ExtFieldProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ExtFieldProperties.class)
public class ExtFieldAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AssembleEnumHandler enumHandler() {
        return new AssembleEnumHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public AssembleSpELHandler spELHandler() {
        return new AssembleSpELHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public AssembleFunctionHandler functionHandler() {
        return new AssembleFunctionHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public ILog log() {
        return new Slf4jLog();
    }


}
