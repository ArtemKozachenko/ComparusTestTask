package com.my.comparustesttask.config;

import com.my.comparustesttask.postprocessor.DynamicBeanDefinitionRegistrarPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class DynamicBeanDefinitionRegistrarConfiguration {
    @Bean
    public static DynamicBeanDefinitionRegistrarPostProcessor beanDefinitionRegistrar(Environment environment) {
        return new DynamicBeanDefinitionRegistrarPostProcessor(environment);
    }

}
