package com.zty.common.dubbo.provider;


import com.zty.common.dubbo.dto.InterfaceInfoDubboDTO;

import java.util.Map;

/**
 * 接口服务
 * @author ZhangTianYou
 */
public interface InterfaceProvider {

    /**
     * 注册活动
     * @param userId    用户id
     */
    void registerWelfare(Long userId);

    /**
     * 根据请求路径 和 请求方式 查询数据
     * @param url       请求路径
     * @param method    请求方式
     * @return
     */
    InterfaceInfoDubboDTO selectByUrlAndMethod(String url,String method);

    /**
     * 查询该用户是否拥有调用该接口权限,并且调用次数 >= 1
     * @param interfaceId       接口 id
     * @param userId            用户 id
     * @return
     */
    Boolean checkUserInterface(Long interfaceId,Long userId);

    /**
     * 用户剩余调用接口次数 -1
     * @param interfaceId       接口 id
     * @param userId            用户 id
     * @return      true：减少成功    false：减少失败
     */
    Boolean reduceUserInterfaceInvokeCount(Long interfaceId,Long userId);

    /**
     * 订单支付成功后的处理
     * @param params       支付宝回调参数
     * @return
     */
    Boolean orderSuccess(Map<String,String> params);

}
