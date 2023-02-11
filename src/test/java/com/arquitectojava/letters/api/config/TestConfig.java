package com.arquitectojava.letters.api.config;

import org.springframework.context.annotation.*;

import java.text.SimpleDateFormat;

//@EnableAutoConfiguration
@Configuration
public class TestConfig {

    @Bean
    public SimpleDateFormat sdf(){
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }
}
