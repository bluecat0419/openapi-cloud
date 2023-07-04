package com.zty.email.consumer;

import com.alibaba.fastjson2.JSON;
import com.rabbitmq.client.Channel;
import static com.zty.common.core.constant.Constant.EmailType;
import com.zty.common.rabbitmq.constant.RabbitConstant;
import com.zty.common.rabbitmq.dto.EmailMessageDTO;
import com.zty.common.redis.keys.RedisKeys;
import com.zty.email.client.EmailClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * email队列的监听器
 * @author ZhangTianYou
 */
@Slf4j
@Service
public class EmailConsumer {

    @Resource
    EmailClient emailClient;

    @Resource
    RedisTemplate redisTemplate;

    @RabbitListener(queues = RabbitConstant.emailQueue)
    public void handleMessage(@Payload Message message, Channel channel){
        //消息体
        String messageBody=new String(message.getBody());
        //当前消息的唯一标识
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            EmailMessageDTO emailDTO = JSON.parseObject(messageBody, EmailMessageDTO.class);

            //验证码邮件类型
            if(EmailType.CODE.equals(emailDTO.getType())){
                //发送验证码
                emailClient.sendSimpleEmail(emailDTO.getTargetEmail(),emailDTO.getSubject(),emailDTO.getContent());
                //缓存验证码,设置两分钟失效时间
                redisTemplate.opsForValue().set(emailDTO.getRedisKey(),emailDTO.getCode(),emailDTO.getExpire(), TimeUnit.MINUTES);
            }else {
                //自定义邮件类型
                if(emailDTO.getFile() == null){
                    emailClient.sendSimpleEmail(emailDTO.getTargetEmail(),emailDTO.getSubject(),emailDTO.getContent());
                }else {
                    InputStream file=new ByteArrayInputStream(emailDTO.getFile());
                    emailClient.sendAttachmentEmail(emailDTO.getTargetEmail(),emailDTO.getSubject(),emailDTO.getContent(), file, emailDTO.getFileName());
                }
            }

            log.info("邮件发送成功,收件人:{}",message.getMessageProperties().getCorrelationId());
            //手动确认消息
            channel.basicAck(deliveryTag,false);
        }catch (Exception e){
            try {
                log.error("消息:{} , deliveryTag:{} 处理异常",message.getMessageProperties().getCorrelationId(),deliveryTag);
                //手动拒收消息basicNack（deliveryTag:唯一标识,multiple:是否多条,requeue:是否重回队列）
                //手动拒收单条消息basicReject（deliveryTag:唯一标识,requeue:是否重回队列）
                //如果不重回队列,则会判断当前队列是否存在死信队列,如果存在则转发到死信队列中,如果不存在,则直接丢弃
                channel.basicReject(deliveryTag,false);
            } catch (Exception ex) {}
        }
    }

}
