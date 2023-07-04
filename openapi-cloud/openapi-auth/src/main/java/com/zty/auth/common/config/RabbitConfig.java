package com.zty.auth.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.zty.common.rabbitmq.constant.QueueArgument.DEAD_EXCHANGE;
import static com.zty.common.rabbitmq.constant.QueueArgument.TTL;
import static com.zty.common.rabbitmq.constant.RabbitConstant.*;

/**
 * RabbitMq的配置类
 * @author ZhangTianYou
 */
@Slf4j
@Configuration
public class RabbitConfig {

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
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
                log.error("消息已发出,交换机已拒收该消息!  correlationData:{}  ,  ack:{}  ,  原因:{}", correlationData, ack, cause);
            }
        });
        return rabbitTemplate;
    }

    /*---------------------声明auth与sms通信的交换机和队列,然后绑定---------------------*/
    @Bean
    public Exchange authToSmsExchange(){
        return new ExchangeBuilder(authToSmsExchange, ExchangeTypes.TOPIC).build();
    }
    @Bean
    public Queue authQueue(){
        Map<String,Object> args=new HashMap<>(3);
        //失效时间  10秒
        args.put(TTL,1000);
        //为auth服务设置死信队列
        args.put(DEAD_EXCHANGE,dlxExchange);
        return new Queue(authQueue,true,false,false,args);
    }
    @Bean
    public Binding authBindingSms(){
        return new Binding(smsQueue, Binding.DestinationType.QUEUE,authToSmsExchange().getName(),smsKey,null);
    }


    /*---------------------声明auth与email通信的交换机和队列,然后绑定---------------------*/
    @Bean
    public Exchange authToEmailExchange(){
        return new ExchangeBuilder(authToEmailExchange, ExchangeTypes.TOPIC).build();
    }
    @Bean
    public Binding authBindingEmail(){
        return new Binding(emailQueue, Binding.DestinationType.QUEUE,authToEmailExchange().getName(),emailKey,null);
    }


    /*---------------------声明死信队列---------------------*/
    @Bean
    public Exchange dlxExchange(){
        return new ExchangeBuilder(dlxExchange,ExchangeTypes.TOPIC).build();
    }
    @Bean
    public Queue dlxQueue(){
        return new Queue(dlxQueue,true);
    }
    @Bean
    public Binding dlxBinding(){
        return new Binding(dlxQueue().getName(), Binding.DestinationType.QUEUE,dlxExchange().getName(),"#",null);
    }

}
