package com.zty.interfaces.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zty.interfaces.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 接口套餐实体类
 * @author ZhangTianYou
 */
@Data
@TableName("interface_package")
public class InterfacePackageEntity extends BaseEntity {

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

    /**
     * 套餐名称
     */
    private String name;

    /**
     * 接口调用次数
     */
    private Integer invokeCount;

    /**
     * 价格
     */
    private Double price;

    /**
     * 折扣
     */
    private Double discount;

    /**
     * 过期时间
     */
    private Date expireDate;

    /**
     * 状态（0-可用，1-不可用）
     */
    private Integer status;

}
