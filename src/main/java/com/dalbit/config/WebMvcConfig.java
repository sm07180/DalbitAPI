package com.dalbit.config;

import com.dalbit.interceptor.ParamCheckInterceptor;
import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ParamCheckInterceptor())
                .addPathPatterns("/**");
    }

    /*@Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "DELETE", "OPTIONS")
                .allowCredentials(false)
                .allowedHeaders("Origin","Accept,X-Requested-With","Content-Type","Access-Control-Request-Method","Access-Control-Request-Headers","Authorization")
                .maxAge(360);
    }*/
}
