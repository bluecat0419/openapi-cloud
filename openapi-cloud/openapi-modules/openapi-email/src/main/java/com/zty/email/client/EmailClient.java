package com.zty.email.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;

/**
 *
 * @author ZhangTianYou
 * @date 2022/11/8
 */
@Slf4j
@Component
public class EmailClient {

    @Resource
    private JavaMailSenderImpl mailSender;

    @Value("${spring.mail.username}")
    private String fromEmailAddress;

    /**
     * 发送纯文本邮件
     * @param targetEmail 收件人地址
     * @param subject  邮件主题
     * @param content  邮件内容
     */
    public void sendSimpleEmail(String targetEmail, String subject, String content) throws Exception {
        //定制纯文本邮件信息SimpleMailMessage
        SimpleMailMessage message=new SimpleMailMessage();
        try {
            //设置发件箱
            message.setFrom(fromEmailAddress);
            //设置收件箱
            message.setTo(targetEmail);
            //设置邮件主题
            message.setSubject(subject);
            //设置邮件内容
            message.setText(content);
            //调用Java封装好的发送方法
            mailSender.send(message);
        }catch (Exception e){
            throw e;
        }
    }


    /**
     * 携带附件发送邮件
     * @param targetEmail 收件人地址
     * @param subject  邮件主题
     * @param content  邮件内容
     * @param file      附件
     */
    public void sendAttachmentEmail(String targetEmail, String subject, String content, InputStream file,String fileName) throws Exception {
        //定制复杂邮件信息MimeMessage
        MimeMessage message=mailSender.createMimeMessage();
        try {
            //使用MimeMessageHelper帮助类，并设置multipart多部件使用为true。帮助将邮件信息封装到MimeMessage message中
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            //设置发件箱
            helper.setFrom(fromEmailAddress);
            //设置收件箱
            helper.setTo(targetEmail);
            //设置邮件主题
            helper.setSubject(subject);
            //设置邮件内容
            helper.setText(content,true);
            //设置邮件附件
            helper.addAttachment(fileName, new ByteArrayResource(IOUtils.toByteArray(file)));
            //发送邮件
            mailSender.send(message);
        } catch (Exception e) {
            throw e;
        }
    }

}
