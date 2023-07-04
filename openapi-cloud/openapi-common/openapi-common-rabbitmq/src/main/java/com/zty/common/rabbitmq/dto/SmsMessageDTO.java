package com.zty.common.rabbitmq.dto;

import com.zty.common.rabbitmq.constant.SmsTemplate;
import lombok.Data;

import java.io.Serializable;

/**
 * 用于向sms服务发送信息的对象
 * @author ZhangTianYou
 */
@Data
public class SmsMessageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 短信模板类型
     */
    private SmsTemplate template;

    /**
     * redis key
     */
    private String redisKey;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 验证码长度
     */
    private Integer codeSize;

    /**
     * 过期时间 单位：分钟
     */
    private Integer expire;

}
