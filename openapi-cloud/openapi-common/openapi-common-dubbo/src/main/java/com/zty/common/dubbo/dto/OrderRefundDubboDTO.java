package com.zty.common.dubbo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单退款入参对象
 * @author ZhangTianYou
 */
@Data
public class OrderRefundDubboDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单号(自定义)
     */
    private String orderNo;

    /**
     * 交易号(支付平台方)
     */
    private String tradeNo;

    /**
     * 退款金额
     */
    private String refundAmount;

    /**
     * 退款原因
     */
    private String refundReason;

    /**
     * 退款请求号    防止该笔交易重复退款
     */
    private String requestNo;

}
