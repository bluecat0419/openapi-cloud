package com.zty.interfaces.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口信息分页VO,用于首页展示
 * @author ZhangTianYou
 */
@Data
@ApiModel("接口信息分页VO,用于主页展示")
public class InterfaceHomePageVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("接口名称")
    private String name;

    @ApiModelProperty("评分")
    private Double score;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("价格")
    private String price;

    @ApiModelProperty("成交量")
    private Integer dealCount;

}
