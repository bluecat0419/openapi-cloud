package com.zty.interfaces.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 文档标题 VO对象
 * @author ZhangTianYou
 */
@Data
@ApiModel("文档标题 VO对象")
public class DocumentTitleVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("文档标题")
    private String title;

}
