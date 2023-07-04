package com.zty.interfaces.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单信息VO
 * @author ZhangTianYou
 */
@Data
@ApiModel("订单信息VO")
public class GenerateOrderInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("接口名称")
    private String interfaceName;

    @ApiModelProperty(value = "接口套餐 id")
    private Long interfacePackageId;

    @ApiModelProperty("套餐名称")
    private String packageName;

    @ApiModelProperty("数量")
    private Integer count;

    @ApiModelProperty("折扣")
    private Double discount;

    @ApiModelProperty("总价格")
    private Double price;

}
