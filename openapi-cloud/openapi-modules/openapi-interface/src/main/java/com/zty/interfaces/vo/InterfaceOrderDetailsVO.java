package com.zty.interfaces.vo;

import com.zty.interfaces.entity.InterfaceOrdersEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单详情VO
 * @author ZhangTianYou
 */
@Data
@ApiModel("订单详情VO")
public class InterfaceOrderDetailsVO extends InterfaceOrdersEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("接口套餐名称")
    private String interfacePackageName;

}
