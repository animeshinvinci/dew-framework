package com.tairanchina.csp.dew.core.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ConditionalOnWebApplication
public class DewConfigurer extends WebMvcConfigurerAdapter {

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        DewHandlerInterceptor dewHandlerInterceptor=  new DewHandlerInterceptor();
        dewHandlerInterceptor.setApplicationName(applicationName);
        registry.addInterceptor(dewHandlerInterceptor).excludePathPatterns("/error/**");
        super.addInterceptors(registry);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseTrailingSlashMatch(true);
    }
}
