package com.webchat.netnest.Config;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.EnumSet;

@Configuration
public class CacheHandlerMappingIntrospectorConfig {

    @Bean
    static FilterRegistrationBean<Filter> handlerMappingInstrspectorConfig(HandlerMappingIntrospector hmi){
        Filter cachFilter = hmi.createCacheFilter();
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>(cachFilter);
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registrationBean.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
    return registrationBean;
    }
}
