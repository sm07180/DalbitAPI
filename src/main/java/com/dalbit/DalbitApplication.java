package com.dalbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class DalbitApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DalbitApplication.class, args);
        //String test = "id=1234567890&asd=";
        //System.out.println(test.substring(3, test.indexOf("&")));
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DalbitApplication.class);
    }
}
