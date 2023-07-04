package com.zty.interfaces.vo;

import com.zty.interfaces.entity.InterfacePackageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口套餐详情 VO
 * @author ZhangTianYou
 */
@Data
@ApiModel("接口套餐详情 VO")
public class InterfacePackageDetails extends InterfacePackageEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("是否设置为主页显示价格")
    private Boolean showPrice;

}
