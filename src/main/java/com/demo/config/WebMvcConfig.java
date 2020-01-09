package com.demo.config;

import com.demo.interceptor.ParamCheckInterceptor;
import io.swagger.models.HttpMethod;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ParamCheckInterceptor())
                .addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("*")
                //.allowedMethods(HttpMethod.POST.name())
                .allowCredentials(false)
                .maxAge(360);
    }
}
