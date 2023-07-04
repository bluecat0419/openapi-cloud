package com.zty.interfaces.request;

import com.zty.interfaces.entity.UserInterfaceInfoEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * 添加新用户注册奖励方法入参
 * @author ZhangTianYou
 */
@Data
@ApiModel(value = "添加新用户注册奖励方法入参")
public class AddRegisterWelfareRequest {

    @ApiModelProperty(value = "接口 id")
    @NotNull(message = "接口 id 不能为空")
    private Long interfaceId;

    @ApiModelProperty(value = "赠送调用次数")
    @Min(value = 1,message = "赠送次数最小为 1")
    private Integer count;

    /**
     * 转换为 UserInterfaceInfoEntity 对象
     * @param userId
     * @return
     */
    public UserInterfaceInfoEntity toUserInterfaceInfoEntity(Long userId){
        UserInterfaceInfoEntity entity = new UserInterfaceInfoEntity();
        entity.setInterfaceInfoId(this.interfaceId);
        entity.setUserId(userId);
        entity.setTotalNum(this.count);
        entity.setLeftNum(this.count);
        return entity;
    }

}
