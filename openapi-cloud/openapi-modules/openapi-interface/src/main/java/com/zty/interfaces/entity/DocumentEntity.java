package com.zty.interfaces.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zty.interfaces.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 系统文档信息表
 * @author ZhangTianYou
 */
@Data
@TableName("document")
public class DocumentEntity extends BaseEntity {

    /**
     * 文档标题
     */
    private String title;

    /**
     * 文档内容
     */
    private String content;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 更新人
     */
    private Long updater;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 是否删除(0-未删, 1-已删)
     */
    private Integer isDeleted;

}
