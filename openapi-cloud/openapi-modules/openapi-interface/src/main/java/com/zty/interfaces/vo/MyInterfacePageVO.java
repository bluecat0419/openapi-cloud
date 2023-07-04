package com.zty.interfaces.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 我的接口分页VO
 * @author ZhangTianYou
 */
@Data
@ApiModel("我的接口分页VO")
public class MyInterfacePageVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("接口id")
    private Long interfaceInfoId;

    @ApiModelProperty("接口名称")
    private String interfaceName;

    @ApiModelProperty("总次数")
    private Integer totalNum;

    @ApiModelProperty("剩余次数")
    private Integer leftNum;

    @ApiModelProperty("状态  1:可用   0:不可用")
    private Integer status;

    @ApiModelProperty("购买时间")
    private Date createDate;

}
