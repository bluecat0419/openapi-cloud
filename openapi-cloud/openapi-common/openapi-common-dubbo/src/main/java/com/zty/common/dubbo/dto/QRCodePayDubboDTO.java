package com.zty.common.dubbo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 扫码支付入参对象
 * @author ZhangTianYou
 */
@Data
public class QRCodePayDubboDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String storeId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单标题
     */
    private String subject;

    /**
     * 总金额
     */
    private String totalAmount;

    /**
     * 订单描述
     */
    private String body;

}
