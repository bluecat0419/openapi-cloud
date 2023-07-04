package com.zty.auth.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 公共实体类
 * @author 张天佑
 */
@Data
public abstract class BaseEntity implements Serializable {


    @TableId
    private Long id;


    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creator;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;

}
