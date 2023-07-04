package com.zty.common.rabbitmq.constant;

/**
 * RabbitMq的交换机和队列和routingKey
 * @author ZhangTianYou
 */
public interface RabbitConstant {

    /**
     * auth服务与sms服务相互通信的交换机
     */
    String authToSmsExchange="exchange.auth.sms";

    /**
     * auth服务与email服务相互通信的交换机
     */
    String authToEmailExchange="exchange.auth.email";

    /**
     * 死信交换机
     */
    String dlxExchange="exchange.dlx";

    /**
     * auth服务的队列名
     */
    String authQueue="queue.auth";

    /**
     * sms服务的队列名
     */
    String smsQueue="queue.sms";

    /**
     * email邮箱服务的队列名
     */
    String emailQueue="queue.email";

    /**
     * 死信队列
     */
    String dlxQueue="queue.dlx";

    /**
     * routingKey
     */
    String authKey="key.auth";

    /**
     * routingKey
     */
    String smsKey="key.sms";

    /**
     * routingKey
     */
    String emailKey="key.email";

    /**
     * 订单交换机
     */
    String orderExchange = "exchange.order";

    /**
     * 订单队列,用于处理订单超时
     */
    String orderQueue = "queue.order";

    /**
     * 订单routingKey
     */
    String orderKey = "key.order";


    /**
     * 处理订单超时的死信交换机
     */
    String orderExpireExchange = "exchange.order.expire";

    /**
     * 处理订单超时的死信队列
     */
    String orderExpireQueue = "queue.order.expire";



}
