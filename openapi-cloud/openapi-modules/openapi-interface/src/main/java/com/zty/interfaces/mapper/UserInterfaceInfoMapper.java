package com.zty.interfaces.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zty.interfaces.entity.UserInterfaceInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户调用接口关系
 * @author ZhangTianYou
 */
@Mapper
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfoEntity> {

    /**
     * 校验用户是否拥有调用改接口权限,并且调用次数 >= 1
     * @param interfaceId       接口 id
     * @param userId            用户 id
     * @return      1：拥有权限,并且调用次数 >= 1     null：不可用或调用次数 < 1
     */
    Integer checkUserInterface(@Param("interfaceId") Long interfaceId, @Param("userId") Long userId);

    /**
     * 用户调用接口次数 -1
     * @param interfaceId       接口 id
     * @param userId            用户 id
     * @return
     */
    Integer reduceUserInterfaceInvokeCount(@Param("interfaceId") Long interfaceId, @Param("userId") Long userId);

    /**
     * 根据用户 id 和接口套餐 id 查询数据
     * @param userId                用户 id
     * @param interfacePackageId    接口套餐 id
     * @return
     */
    UserInterfaceInfoEntity selectByUserIdAndPackageId(@Param("userId") Long userId, @Param("interfacePackageId") Long interfacePackageId);

}
