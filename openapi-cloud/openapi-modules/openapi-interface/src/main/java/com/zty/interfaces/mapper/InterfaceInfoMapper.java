package com.zty.interfaces.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zty.interfaces.entity.InterfaceInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 接口信息
 * @author ZhangTianYou
 */
@Mapper
public interface InterfaceInfoMapper extends BaseMapper<InterfaceInfoEntity> {

    /**
     * 根据套餐 id 查询接口信息
     * @param packageId     套餐 id
     * @return
     */
    InterfaceInfoEntity selectByPackageId(@Param("packageId") Long packageId);

}
