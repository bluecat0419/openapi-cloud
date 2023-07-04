package com.zty.interfaces.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 评论方法入参
 * @author ZhangTianYou
 */
@Data
@ApiModel("评论方法入参")
public class CommentRequest {

    @ApiModelProperty(value = "接口 id")
    @NotNull(message = "接口 id 不能为空")
    private Long interfaceInfoId;

    @ApiModelProperty(value = "评论")
    @NotNull(message = "评论不能为空")
    private String comment;

    @ApiModelProperty(value = "评分")
    @NotNull(message = "评分不能为空")
    private Double score;

}
