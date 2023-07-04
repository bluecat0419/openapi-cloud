package com.zty.interfaces.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口评论VO
 * @author ZhangTianYou
 */
@Data
@ApiModel(value = "接口评论VO")
public class InterfaceCommentVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "用户名(脱敏后)")
    private String username;

    @ApiModelProperty(value = "评论内容")
    private String comment;

    @ApiModelProperty(value = "评分")
    private Double score;

    @ApiModelProperty(value = "评论时间")
    private Date createDate;

}
