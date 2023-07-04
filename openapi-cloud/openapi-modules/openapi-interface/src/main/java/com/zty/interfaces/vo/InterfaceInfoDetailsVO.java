package com.zty.interfaces.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 接口信息详情 VO
 * @author ZhangTianYou
 */
@Data
@ApiModel("接口信息详情 VO")
public class InterfaceInfoDetailsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("接口名称")
    private String name;

    @ApiModelProperty("接口描述")
    private String description;

    @ApiModelProperty("接口地址")
    private String url;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("请求参数")
    private List<InterfaceDocVO.Param> requestParamList;

    @ApiModelProperty("请求体参数")
    private List<InterfaceDocVO.Param> requestBodyList;

    @ApiModelProperty("请求头参数")
    private List<InterfaceDocVO.Param> requestHeaderList;

    @ApiModelProperty("响应类型")
    private String responseType;

    @ApiModelProperty("返回数据")
    private String responseData;

    @ApiModelProperty("接口状态（0-开启，1-关闭）")
    private Integer status;

    @ApiModelProperty("接口请求类型")
    private String method;

    @ApiModelProperty("代码示例")
    private String codeDemo;

    @ApiModelProperty(value = "是否删除  0:未删   1:已删")
    private Integer isDeleted;

    @ApiModelProperty(value = "调用次数")
    private Integer invokeCount;

}
