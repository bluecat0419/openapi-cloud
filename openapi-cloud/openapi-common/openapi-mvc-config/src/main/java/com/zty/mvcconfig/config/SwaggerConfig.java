package com.zty.mvcconfig.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Swagger 配置类
 * 原生:/swagger-ui.html
 * 美化:/doc.html
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * Swagger 实例 Bean 是 Docket，所以通过配置 Docket 实例来配置 Swagger
     * @return
     */
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zty"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * Swagger 的描述信息
     * @return
     */
    public ApiInfo apiInfo(){
        return new ApiInfoBuilder().title("API开放平台")
                .description("API开放平台接口文档")
                .contact(new Contact("张天佑","nothing...","2869437984@qq.com"))
                .version("1.0")
                .build();
    }

}
