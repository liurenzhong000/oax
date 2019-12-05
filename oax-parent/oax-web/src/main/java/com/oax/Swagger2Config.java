package com.oax;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Profile("test")
@Configuration
@EnableSwagger2
@ComponentScan(basePackages = { "com.oax.controller" })
public class Swagger2Config {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("WEB系统Restful API")
                .description("WEB系统Restful API")
                .termsOfServiceUrl("http://127.0.0.1:8080/")
                .version("1.0")
                .build();
    }
}
