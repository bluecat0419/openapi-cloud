package com.zty.interfaces.request;

import com.zty.common.core.groups.SaveGroup;
import com.zty.common.core.groups.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * 保存或修改系统文档请求对象
 * @author ZhangTianYou
 */
@Data
@ApiModel(value = "保存或修改系统文档请求对象")
public class SaveOrUpdateDocumentRequest {

    @Null(message = "id 必须为空",groups = {SaveGroup.class})
    @NotNull(message = "id 不能为空",groups = {UpdateGroup.class})
    private Long id;

    @ApiModelProperty(value = "文档标题")
    @Size(groups = {SaveGroup.class,UpdateGroup.class},min = 1,message = "接口名称不能为空")
    private String title;

    @ApiModelProperty(value = "文档内容")
    private String content;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}
