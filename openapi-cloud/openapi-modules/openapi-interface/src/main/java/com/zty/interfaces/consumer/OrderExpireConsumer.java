package com.zty.interfaces.consumer;

import com.rabbitmq.client.Channel;
import com.zty.common.core.constant.OrderStatusEnum;
import com.zty.common.rabbitmq.constant.RabbitConstant;
import com.zty.interfaces.entity.InterfaceOrdersEntity;
import com.zty.interfaces.service.InterfaceOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 订单超时消费者
 */
@Slf4j
@Service
public class OrderExpireConsumer {

    @Resource
    InterfaceOrdersService interfaceOrdersService;

    @RabbitListener(queues = RabbitConstant.orderExpireQueue)
    public void handleMessage(@Payload Message message, Channel channel){
        String orderNo=new String(message.getBody());
        //当前消息的唯一标识
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            //查询订单状态
            InterfaceOrdersEntity order = interfaceOrdersService.selectByOrderNo(orderNo);
            //如果订单状态为已支付或已退款,则不做处理,否则修改订单状态为超时
            if(order.getStatus() == OrderStatusEnum.SUCCESS.getValue() || order.getStatus() == OrderStatusEnum.REFUND.getValue()){
                channel.basicAck(deliveryTag,false);
                return;
            }

            //修改订单状态为超时
            Boolean flag = interfaceOrdersService.updateOrderStatus(orderNo, OrderStatusEnum.TIMEOUT.getValue());
            if(flag){
                log.info("订单:[{}]已超时",orderNo);
                channel.basicAck(deliveryTag,false);
            }else {
                channel.basicReject(deliveryTag,false);
            }
        } catch (Exception e) {
            log.error("处理超时订单失败"+e.getMessage(),e);
            try {
                channel.basicReject(deliveryTag,false);
            } catch (IOException ex) {}
        }
    }

}
