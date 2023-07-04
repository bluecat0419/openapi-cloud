package com.zty.interfaces.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zty.interfaces.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 接口订单实体类
 * @author ZhangTianYou
 */
@Data
@TableName("interface_orders")
public class InterfaceOrdersEntity extends BaseEntity {

    /**
     * 订单号(自定义)
     */
    private String orderNo;

    /**
     * 交易号(支付平台方)
     */
    private String tradeNo;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 接口套餐 id
     */
    private Long interfacePackageId;

    /**
     * 创建订单时的套餐信息
     */
    private String packageInfo;

    /**
     * 套餐购买数量
     */
    private Integer count;

    /**
     * 总金额
     */
    private Double totalPrice;

    /**
     * 支付二维码
     */
    private String qrCode;

    /**
     * 支付方式 0：支付宝
     */
    private Integer payType;

    /**
     * 支付信息  json
     */
    private String payInfo;

    /**
     * 退款信息  json
     */
    private String refundInfo;

    /**
     * 订单状态   (0：订单提交  -1：订单已退款   1：支付成功   2：订单超时)
     */
    private Integer status;

    /**
     * 付款时间
     */
    private Date paymentDate;

    /**
     * 是否删除 0：未删除  1：已删除
     */
    private Integer isDeleted;

}
