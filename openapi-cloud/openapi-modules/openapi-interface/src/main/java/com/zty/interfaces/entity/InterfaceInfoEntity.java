package com.zty.interfaces.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zty.interfaces.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 接口信息
 * @author ZhangTianYou
 */
@Data
@TableName("interface_info")
public class InterfaceInfoEntity extends BaseEntity {

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 图标
     */
    private String icon;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求体
     */
    private String requestBody;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 响应类型
     */
    private String responseType;

    /**
     * 返回数据
     */
    private String responseData;

    /**
     * 接口状态（0-开启，1-关闭）
     */
    private Integer status;

    /**
     * 接口请求类型
     */
    private String method;

    /**
     * 代码示例
     */
    private String codeDemo;

    /**
     * 修改人
     */
    private Long updater;

    /**
     * 修改时间
     */
    private Date updateDate;

    /**
     * 是否删除  0:未删   1:已删
     */
    private Integer isDeleted;

}
