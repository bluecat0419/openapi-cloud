package com.zty.interfaces.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 订单退款请求对象
 * @author ZhangTianYou
 */
@Data
@ApiModel("订单退款请求对象")
public class OrderRefundRequest {

    @ApiModelProperty(value = "订单 id")
    @NotNull(message = "订单 id 不能为空")
    private Long id;

    @ApiModelProperty(value = "退款原因")
    private String refundReason;

}
