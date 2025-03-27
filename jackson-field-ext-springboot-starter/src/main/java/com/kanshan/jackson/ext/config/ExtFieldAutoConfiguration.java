package com.kanshan.jackson.ext.config;

import com.kanshan.jackson.ext.core.handler.AssembleEnumHandler;
import com.kanshan.jackson.ext.core.handler.AssembleFunctionHandler;
import com.kanshan.jackson.ext.core.handler.AssembleSpELHandler;
import com.kanshan.jackson.ext.core.properties.JacksonFieldExtProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JacksonFieldExtProperties.class)
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


}
