package com.zty.interfaces.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zty.common.core.page.PageData;
import com.zty.interfaces.entity.InterfaceInfoEntity;
import com.zty.interfaces.request.AddRegisterWelfareRequest;
import com.zty.interfaces.request.SaveOrUpdateInterfaceRequest;
import com.zty.interfaces.vo.*;

import java.util.Map;

/**
 * 接口信息
 * @author ZhangTianYou
 */
public interface InterfaceService extends IService<InterfaceInfoEntity> {

    /**
     * 分页
     * @param params
     * @return
     */
    PageData<InterfaceInfoVO> page(Map<String, Object> params);

    /**
     * 详情
     * @param id
     * @return
     */
    InterfaceInfoDetailsVO get(Long id);

    /**
     * 保存
     * @param request
     * @return
     */
    Long save(SaveOrUpdateInterfaceRequest request);

    /**
     * 修改
     * @param request
     * @return
     */
    Boolean update(SaveOrUpdateInterfaceRequest request);

    /**
     * 逻辑删除
     * @param id
     * @return
     */
    boolean fakeDelete(Long id);


    /**
     * 添加新用户注册奖励
     * @param request
     * @return
     */
    boolean addRegisterWelfare(AddRegisterWelfareRequest request);

    /**
     * 首页：分页
     * @param params
     * @return
     */
    PageData<InterfaceHomePageVO> homepage(Map<String, Object> params);

    /**
     * 首页：接口详情
     * @param id
     * @return
     */
    InterfaceHomeDetailVO details(Long id);

    /**
     * 接口文档信息
     * @param id    接口id
     * @return
     */
    InterfaceDocVO interfaceDoc(Long id);

}
