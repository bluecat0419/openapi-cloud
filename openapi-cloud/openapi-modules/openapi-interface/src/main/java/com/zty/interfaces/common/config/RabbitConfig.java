package com.zty.interfaces.common.config;

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

    /*---------------------订单队列,用于处理订单超时---------------------*/
    @Bean
    public Exchange orderExchange(){
        return new ExchangeBuilder(orderExchange, ExchangeTypes.TOPIC).build();
    }
    @Bean
    public Queue orderQueue(){
        Map<String,Object> args=new HashMap<>(3);
        //失效时间  24小时
        args.put(TTL,86400000);
//        args.put(TTL,60000);
        //为order设置订单超时死信队列
        args.put(DEAD_EXCHANGE,orderExpireExchange);
        return new Queue(orderQueue,true,false,false,args);
    }
    @Bean
    public Binding orderBindingKey(){
        return new Binding(orderQueue, Binding.DestinationType.QUEUE,orderExchange().getName(),orderKey,null);
    }


    /*---------------------声明处理超时订单的死信队列---------------------*/
    @Bean
    public Exchange orderExpireExchange(){
        return new ExchangeBuilder(orderExpireExchange,ExchangeTypes.TOPIC).build();
    }
    @Bean
    public Queue orderExpireQueue(){
        return new Queue(orderExpireQueue,true);
    }
    @Bean
    public Binding orderExpireBindingKey(){
        return new Binding(orderExpireQueue, Binding.DestinationType.QUEUE,orderExpireExchange,"#",null);
    }

}
