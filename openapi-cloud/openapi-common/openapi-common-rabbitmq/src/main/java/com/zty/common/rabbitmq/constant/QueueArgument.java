package com.zty.common.rabbitmq.constant;

/**
 * RabbitMQ队列特殊参数常量
 * @author ZhangTianYou
 */
public interface QueueArgument{
    /**
     * 失效时间
     */
    String TTL="x-message-ttl";

    /**
     * 最大长度
     */
    String MAX_LENGTH="x-max-length";

    /**
     * 死信交换机(成为死信后转发的交换机)
     */
    String DEAD_EXCHANGE="x-dead-letter-exchange";

    /**
     * 死信路由键(成为死信后转发的路由键)
     */
    String DEAD_ROUTING_KEY="x-dead-letter-routing-key";
}
