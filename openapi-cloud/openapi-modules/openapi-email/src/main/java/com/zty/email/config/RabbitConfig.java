package com.zty.email.config;

import static com.zty.common.rabbitmq.constant.QueueArgument.*;
import static com.zty.common.rabbitmq.constant.RabbitConstant.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhangTianYou
 * @date 2022/10/26
 */
@Slf4j
@Configuration
public class RabbitConfig {


    /**
     * 声明当前服务队列
     * @return
     */
    @Bean
    public Queue emailQueue() {
        Map<String, Object> args = new HashMap<>(3);
        //失效时间  60秒
        args.put(TTL, 6000);
        //最大长度
        args.put(MAX_LENGTH, 100);
        //为email服务设置死信队列
        args.put(DEAD_EXCHANGE, dlxExchange);
        return new Queue(emailQueue, true, false, false, args);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        //激活rabbitmq
        connectionFactory.createConnection();
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        //如果消息没有成功路由到队列中,那么就会调用此方法,消息返回机制
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.info("消息没有成功路由到队列中!   message:{}  ,  replyCode:{}  ,  replyText:{}  ,  exchange:{}  ,  routingKey:{}",
                    new String(message.getBody()), replyCode, replyText, exchange, routingKey);
        });

        //如果消息已经被交换机接收到,那么就会调用此方法,消息确认机制
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if(ack){
                log.info("消息已发出,交换机已收到该消息!  correlationData:{}  ,  ack:{}  ,  cause:{}", correlationData, ack, cause);
            }else {
                log.warn("消息已发出,交换机已拒收该消息!  correlationData:{}  ,  ack:{}  ,  原因:{}", correlationData, ack, cause);
            }
        });
        return rabbitTemplate;
    }

}
