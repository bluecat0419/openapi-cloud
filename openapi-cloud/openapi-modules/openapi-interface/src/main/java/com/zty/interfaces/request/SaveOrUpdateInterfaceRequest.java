package com.zty.interfaces.request;

import com.zty.common.core.groups.SaveGroup;
import com.zty.common.core.groups.UpdateGroup;
import com.zty.interfaces.vo.InterfaceDocVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 接口 新增/修改方法 入参
 * @author ZhangTianYou
 */
@Data
@ApiModel("接口 新增/修改 入参")
public class SaveOrUpdateInterfaceRequest {

    @NotNull(groups = UpdateGroup.class,message = "接口id不能为空")
    @Null(groups = SaveGroup.class,message = "接口id必须为空")
    private Long id;

    @ApiModelProperty(value = "接口名称")
    @Size(groups = {SaveGroup.class,UpdateGroup.class},min = 1,message = "接口名称不能为空")
    private String name;

    @ApiModelProperty(value = "接口图标")
    private String icon;

    @ApiModelProperty(value = "接口描述")
    private String description;

    @ApiModelProperty(value = "接口地址")
    @Size(groups = {SaveGroup.class,UpdateGroup.class},min = 1,message = "接口地址不能为空")
    private String url;

    @ApiModelProperty(value = "请求参数")
    private List<InterfaceDocVO.Param> requestParamList;

    @ApiModelProperty(value = "请求体参数")
    private List<InterfaceDocVO.Param> requestBodyList;

    @ApiModelProperty(value = "请求头参数")
    private List<InterfaceDocVO.Param> requestHeaderList;

    @ApiModelProperty(value = "响应头")
    private String responseHeader;

    @ApiModelProperty(value = "响应类型")
    private String responseType;

    @ApiModelProperty(value = "返回数据")
    private String responseData;

    @ApiModelProperty(value = "接口请求类型")
    @Size(groups = {SaveGroup.class,UpdateGroup.class},min = 1,message = "接口请求类型不能为空")
    private String method;

    @ApiModelProperty(value = "接口状态 (0：关闭   1：开启)")
    @NotNull(groups = {SaveGroup.class,UpdateGroup.class},message = "接口状态不能为空")
    private Integer status;

    @ApiModelProperty(value = "代码示例")
    private String codeDemo;

}
