package com.zty.interfaces.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口调用次数分析 VO
 * @author ZhangTianYou
 */
@Data
@ApiModel(value = "接口调用次数分析 VO")
public class InvokeCountAnalysisVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "接口id")
    private Long id;

    @ApiModelProperty(value = "接口名称")
    private String name;

    @ApiModelProperty(value = "调用次数")
    private Integer invokeCount;

}
