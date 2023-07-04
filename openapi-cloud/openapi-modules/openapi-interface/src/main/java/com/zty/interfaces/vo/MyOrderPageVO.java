package com.zty.interfaces.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户的订单分页VO
 * @author ZhangTianYou
 */
@Data
@ApiModel("用户的订单分页VO")
public class MyOrderPageVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("接口名称")
    private String interfaceName;

    @ApiModelProperty("接口图标")
    private String icon;

    @ApiModelProperty("套餐名称")
    private String packageName;

    @ApiModelProperty("数量")
    private Integer count;

    @ApiModelProperty("价格")
    private Double price;

    @ApiModelProperty("订单状态")
    private Integer status;

    @ApiModelProperty("付款时间")
    private Date paymentDate;

}
