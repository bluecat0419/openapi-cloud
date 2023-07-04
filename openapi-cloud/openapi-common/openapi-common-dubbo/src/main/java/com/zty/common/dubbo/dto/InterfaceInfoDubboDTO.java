package com.zty.common.dubbo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用于在 dubbo 服务与服务之间传输的对象
 * @author ZhangTianYou
 */
@Data
public class InterfaceInfoDubboDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

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
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 返回数据 json 格式
     */
    private String responseData;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

}
