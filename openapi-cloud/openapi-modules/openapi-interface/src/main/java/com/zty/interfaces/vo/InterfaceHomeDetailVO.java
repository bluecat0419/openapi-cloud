package com.zty.interfaces.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 首页接口详细信息 VO,用于首页展示
 * @author ZhangTianYou
 */
@Data
@ApiModel("首页接口详细信息 VO")
public class InterfaceHomeDetailVO implements Serializable {
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

    @ApiModelProperty("套餐集合")
    private List<PackageVO> packages;

    /**
     * 套餐信息
     * @author ZhangTianYou
     */
    @Data
    @ApiModel("套餐信息")
    public static class PackageVO{
        @ApiModelProperty("套餐id")
        private Long packageId;

        @ApiModelProperty("套餐名称")
        private String packageName;

        @ApiModelProperty("套餐价格")
        private String price;
    }

}
