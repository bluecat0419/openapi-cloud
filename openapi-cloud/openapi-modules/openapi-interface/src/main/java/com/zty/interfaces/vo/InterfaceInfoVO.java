package com.zty.interfaces.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口信息 vo 对象
 * @author ZhangTianYou
 */
@Data
@ApiModel(value = "接口信息 vo 对象")
public class InterfaceInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "接口名称")
    private String name;

    @ApiModelProperty(value = "接口描述")
    private String description;

    @ApiModelProperty(value = "接口地址")
    private String url;

    @ApiModelProperty(value = "请求参数")
    private String requestParams;

    @ApiModelProperty(value = "请求体")
    private String requestBody;

    @ApiModelProperty(value = "请求头")
    private String requestHeader;

    @ApiModelProperty(value = "响应头")
    private String responseHeader;

    @ApiModelProperty(value = "响应类型")
    private String responseType;

    @ApiModelProperty(value = "返回数据 json 格式")
    private String responseData;

    @ApiModelProperty(value = "接口状态（0-开启，1-关闭）")
    private Integer status;

    @ApiModelProperty(value = "接口请求类型  (GET POST)")
    private String method;

    @ApiModelProperty(value = "代码示例")
    private String codeDemo;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

}
