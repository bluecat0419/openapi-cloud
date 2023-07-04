package com.zty.interfaces.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zty.common.core.page.PageData;
import com.zty.interfaces.entity.InterfacePackageEntity;
import com.zty.interfaces.request.SaveOrUpdateInterfacePackageRequest;
import com.zty.interfaces.vo.InterfacePackageDetails;

import java.util.List;
import java.util.Map;

/**
 * 接口套餐
 * @author ZhangTianYou
 */
public interface InterfacePackageService extends IService<InterfacePackageEntity> {

    /**
     * 分页
     * @param params
     * @return
     */
    PageData<InterfacePackageEntity> page(Map<String,Object> params);

    /**
     * 详情
     * @param id
     * @return
     */
    InterfacePackageDetails details(Long id);

    /**
     * 保存
     * @param request
     * @return
     */
    Long save(SaveOrUpdateInterfacePackageRequest request);

    /**
     * 修改
     * @param request
     * @return
     */
    Boolean update(SaveOrUpdateInterfacePackageRequest request);

    /**
     * 设置该套餐为主页显示价格
     * @param id     套餐id
     * @return
     */
    Boolean setShowPrice(Long id);

    /**
     * 根据接口id查询没有过期套餐集合
     * @param interfaceId       接口id
     * @return
     */
    List<InterfacePackageEntity> selectByInterfaceIdAndNotExpired(Long interfaceId);

}
