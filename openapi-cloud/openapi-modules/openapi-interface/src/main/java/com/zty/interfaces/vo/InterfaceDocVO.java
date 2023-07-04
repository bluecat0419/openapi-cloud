package com.zty.interfaces.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 接口文档 VO对象
 * @author ZhangTianYou
 */
@Data
@ApiModel("接口文档 VO对象")
public class InterfaceDocVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("接口名称")
    private String name;

    @ApiModelProperty("接口描述")
    private String description;

    @ApiModelProperty("接口地址")
    private String url;

    @ApiModelProperty("接口请求方式")
    private String method;

    @ApiModelProperty("响应类型")
    private String responseType;

    @ApiModelProperty("请求参数")
    private List<Param> requestParamsList;

    @ApiModelProperty("请求体参数")
    private List<Param> requestBodyList;

    @ApiModelProperty("请求头参数")
    private List<Param> requestHeaderList;

    @ApiModelProperty("代码示例")
    private String codeDemo;

    @ApiModelProperty("返回示例")
    private String responseData;

    @Data
    @ApiModel("请求参数")
    public static class Param{
        @ApiModelProperty("参数名称")
        private String name;
        @ApiModelProperty("参数类型")
        private String type;
        @ApiModelProperty("是否必须")
        private Boolean required;
        @ApiModelProperty("参数描述")
        private String description;
    }

}
