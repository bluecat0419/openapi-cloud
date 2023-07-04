package com.zty.interfaces.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单详情VO
 * @author ZhangTianYou
 */
@Data
@ApiModel("订单详情VO")
public class MyOrderDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("交易号")
    private String tradeNo;

    @ApiModelProperty("接口名称")
    private String interfaceName;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("套餐名称")
    private String packageName;

    @ApiModelProperty("数量")
    private Integer count;

    @ApiModelProperty("总价格")
    private Double price;

    @ApiModelProperty("折扣")
    private Double discount;

    @ApiModelProperty("实付款")
    private Double realPrice;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("付款时间")
    private Date paymentDate;

    @ApiModelProperty("订单状态")
    private Integer status;

}
