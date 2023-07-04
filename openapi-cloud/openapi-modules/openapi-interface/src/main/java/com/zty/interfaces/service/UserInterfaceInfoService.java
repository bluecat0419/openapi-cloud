package com.zty.interfaces.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zty.common.core.page.PageData;
import com.zty.interfaces.entity.UserInterfaceInfoEntity;
import com.zty.interfaces.vo.MyInterfacePageVO;

import java.util.Map;

/**
 * 用户调用接口关系
 * @author ZhangTianYou
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfoEntity> {

    /**
     * 根据用户id 和 接口id 查询数据
     * @param userId        用户id
     * @param interfaceId   接口id
     * @return
     */
    UserInterfaceInfoEntity selectByUserIdAndInterfaceId(Long userId,Long interfaceId);

    /**
     * 我的接口分页
     * @param params
     * @return
     */
    PageData<MyInterfacePageVO> myInterfacePage(Map<String,Object> params);

}
