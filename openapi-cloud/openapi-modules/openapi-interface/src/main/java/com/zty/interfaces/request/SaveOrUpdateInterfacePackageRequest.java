package com.zty.interfaces.request;

import com.zty.common.core.groups.SaveGroup;
import com.zty.common.core.groups.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.Date;
/**
 * 接口套餐 新增/修改方法 入参
 * @author ZhangTianYou
 */
@Data
@ApiModel("接口套餐 新增/修改方法 入参")
public class SaveOrUpdateInterfacePackageRequest {

    @Null(message = "id 必须为空",groups = {SaveGroup.class})
    @NotNull(message = "id 不能为空",groups = {UpdateGroup.class})
    private Long id;

    @NotNull(groups = {SaveGroup.class,UpdateGroup.class},message = "接口 id 不能为空")
    @ApiModelProperty(value = "接口 id")
    private Long interfaceInfoId;

    @Size(groups = {SaveGroup.class,UpdateGroup.class},min = 1,max = 50,message = "套餐名称长度为 1-50 个字符")
    @ApiModelProperty(value = "套餐名称")
    private String name;

    @Min(groups = {SaveGroup.class,UpdateGroup.class},value = 1,message = "调用次数最小为 1")
    @ApiModelProperty(value = "接口调用次数")
    private Integer invokeCount;

    @NotNull(groups = {SaveGroup.class,UpdateGroup.class},message = "价格不能为空")
    @ApiModelProperty(value = "价格")
    private Double price;

    @ApiModelProperty(value = "折扣")
    private Double discount;

    @ApiModelProperty(value = "过期时间")
    private Date expireDate;

    @NotNull(groups = {SaveGroup.class,UpdateGroup.class},message = "状态不能为空")
    @ApiModelProperty(value = "状态（0-可用，1-不可用）")
    private Integer status;

    @ApiModelProperty(value = "是否设置为主页显示价格")
    private Boolean showPrice;

}
