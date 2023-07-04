package com.zty.interfaces.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.zty.interfaces.common.entity.BaseEntity;
import lombok.Data;

/**
 * 用户调用接口关系
 * @author ZhangTianYou
 */
@Data
@TableName("user_interface_info")
public class UserInterfaceInfoEntity extends BaseEntity {

    /**
     * 调用用户 id
     */
    private Long userId;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 0-正常，1-禁用
     */
    private Integer status;

    /**
     * 是否删除(0-未删, 1-已删)
     */
    private Integer isDeleted;

}
