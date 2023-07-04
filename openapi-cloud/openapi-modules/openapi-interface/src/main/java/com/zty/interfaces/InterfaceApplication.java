package com.zty.interfaces;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 *  启动类
 * @author ZhangTianYou
 */
@EnableDubbo
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.zty")
public class InterfaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterfaceApplication.class,args);
    }

}
