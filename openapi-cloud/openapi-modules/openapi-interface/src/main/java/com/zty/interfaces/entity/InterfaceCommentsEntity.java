package com.zty.interfaces.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zty.interfaces.common.entity.BaseEntity;
import lombok.Data;

/**
 * 接口评论
 * @author ZhangTianYou
 */
@Data
@TableName("interface_comments")
public class InterfaceCommentsEntity extends BaseEntity {

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

    /**
     * 评论用户 id
     */
    private Long userId;

    /**
     * 用户名(脱敏后)
     */
    private String username;

    /**
     * 评论内容
     */
    private String comment;

    /**
     * 评分
     */
    private Double score;

    /**
     * 是否删除  0:否  1:是
     */
    private Integer isDeleted;

}
