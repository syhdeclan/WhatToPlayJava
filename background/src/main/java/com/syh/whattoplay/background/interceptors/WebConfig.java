package com.syh.whattoplay.background.interceptors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(AuthorizationInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public AuthorizationInterceptor AuthorizationInterceptor(){
        return new AuthorizationInterceptor();
    }

}
