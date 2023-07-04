package com.zty.interfaces.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 创建订单方法入参
 * @Author ZhangTianYou
 */
@Data
@ApiModel("创建订单方法入参")
public class CreateOrderRequest {

    /**
     * 支付类型 0：支付宝
     */
    @ApiModelProperty(value = "支付类型 0：支付宝")
    @NotNull(message = "支付类型不能为空")
    private Integer payType;

    /**
     * 接口套餐 id
     */
    @ApiModelProperty(value = "接口套餐 id")
    @NotNull(message = "接口套餐 id 不能为空")
    private Long interfacePackageId;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    @Min(value = 1,message = "购买数量最小为 1")
    private Integer count;

}
