package com.zty.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 网关需要注册到容器中的 Bean
 * @author ZhangTianYou
 */
@Configuration
public class GatewayBean {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
