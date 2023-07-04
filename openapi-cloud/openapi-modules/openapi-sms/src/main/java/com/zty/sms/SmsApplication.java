package com.zty.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 短信服务
 * @author ZhangTianYou
 */
@SpringBootApplication(scanBasePackages = "com.zty")
public class SmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmsApplication.class,args);
    }

}
