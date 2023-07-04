package com.zty.auth.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * accessKey
 * @author ZhangTianYou
 */
@Data
@ApiModel(value = "AccessKey对象")
@AllArgsConstructor
@NoArgsConstructor
public class AccessKey implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "accessKey账号")
    private String accessKey;

    @ApiModelProperty(value = "secretKey密码")
    private String secretKey;

}
