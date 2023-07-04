package com.zty.common.rabbitmq.dto;

import com.zty.common.core.constant.Constant.EmailType;
import lombok.Data;

import java.io.Serializable;

/**
 * 用于向email服务发送信息的对象
 * @author ZhangTianYou
 */
@Data
public class EmailMessageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * redis key
     */
    private String redisKey;

    /**
     * 邮件类型
     */
    private EmailType type;

    /**
     * 发送目标邮箱
     */
    private String targetEmail;

    /**
     * 发送主题
     */
    private String subject;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 验证码
     */
    private String code;

    /**
     * 附件
     */
    private byte[] file;
    /**
     * 附件名称
     */
    private String fileName;

    /**
     * 过期时间 单位：分钟
     */
    private Integer expire;

}
