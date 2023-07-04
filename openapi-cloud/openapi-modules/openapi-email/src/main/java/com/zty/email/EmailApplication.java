package com.zty.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 邮件服务启动类
 * @author ZhangTianYou
 */
@SpringBootApplication(scanBasePackages = "com.zty")
public class EmailApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailApplication.class,args);
    }

}
