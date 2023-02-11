package com.arquitectojava.letters.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket apiDocs() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Letters Services")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.arquitectojava.lettersservices"))
                //.paths(PathSelectors.ant("/v1/*"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Letters Services",
                "Servicio iniciales Letters Papeleria SAS",
                "1.X",
                "Terms of service",
                new Contact("Andrés Jiménez", "www.letterspapeleria.co", "andres@letterspapeleria.co"),
                "License of API", "API license URL", Collections.emptyList());
    }



}
