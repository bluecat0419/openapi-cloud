package com.zty.auth.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * 生成API签名请求类
 * @author ZhangTianYou
 */
@Data
@ApiModel("生成API签名请求类")
public class GenAPISignRequest {

    @ApiModelProperty(value = "随机字符串")
    @Size(min = 1,message = "随机字符串不能为空")
    private String nonce;

    @ApiModelProperty(value = "时间戳")
    @Size(min = 1,message = "时间戳不能为空")
    private String timestamp;

}
