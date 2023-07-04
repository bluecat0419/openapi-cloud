package com.zty.pay.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 支付宝交易查询请求参数
 * @author ZhangTianYou
 */
@Data
@ApiModel("支付宝交易查询请求参数")
public class TradeQueryRequest {

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("支付宝交易号")
    private String tradeNo;

}
