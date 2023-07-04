package com.zty.sms.comsumer;

import com.alibaba.fastjson2.JSON;
import com.rabbitmq.client.Channel;
import com.zty.common.core.utils.CodeUtils;
import com.zty.common.rabbitmq.constant.RabbitConstant;
import com.zty.common.rabbitmq.dto.SmsMessageDTO;
import com.zty.sms.client.GYYSmsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 短信服务消费者
 * @author ZhangTianYou
 */
@Slf4j
@Service
public class SmsConsumer {

    @Resource
    RedisTemplate redisTemplate;

    @RabbitListener(queues = RabbitConstant.smsQueue)
    public void handleMessage(@Payload Message message, Channel channel){
        //消息体
        String messageBody=new String(message.getBody());
        //当前消息的唯一标识
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            SmsMessageDTO messageDTO = JSON.parseObject(messageBody, SmsMessageDTO.class);
            //生成验证码
            String code;
            if(messageDTO.getCodeSize() == null){
                code= CodeUtils.getCode();
            }else{
                code= CodeUtils.getCode(messageDTO.getCodeSize());
            }

            //AliyunSmsClient.sendSms(messageDTO.getPhone(),code);
            GYYSmsClient.sendSms(messageDTO,code);

            //将验证码缓存到redis中,设置两分钟失效时间
            redisTemplate.opsForValue().set(messageDTO.getRedisKey(),code,messageDTO.getExpire(), TimeUnit.MINUTES);

            channel.basicAck(deliveryTag,false);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            try {
                log.error("对象:{} 处理异常",message.getMessageProperties().getCorrelationId());
                channel.basicReject(deliveryTag,false);
            } catch (IOException ex) {
                log.error(ex.getMessage(),ex);
            }
        }

    }

}
