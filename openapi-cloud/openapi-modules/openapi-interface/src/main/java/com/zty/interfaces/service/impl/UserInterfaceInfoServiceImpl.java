package com.zty.interfaces.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zty.common.core.constant.InterfaceStatusEnum;
import com.zty.common.core.page.PageData;
import com.zty.common.core.utils.ConvertUtil;
import com.zty.common.core.vo.UserDetails;
import com.zty.interfaces.common.entity.BaseEntity;
import com.zty.interfaces.common.utils.PageUtil;
import com.zty.interfaces.entity.InterfaceInfoEntity;
import com.zty.interfaces.entity.UserInterfaceInfoEntity;
import com.zty.interfaces.mapper.UserInterfaceInfoMapper;
import com.zty.interfaces.service.InterfaceService;
import com.zty.interfaces.service.UserInterfaceInfoService;
import com.zty.interfaces.vo.InterfaceInfoDetailsVO;
import com.zty.interfaces.vo.MyInterfacePageVO;
import com.zty.mvcconfig.context.UserContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户接口关系
 * @author ZhangTianYou
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfoEntity> implements UserInterfaceInfoService {

    @Resource
    InterfaceService interfaceService;

    @Override
    public UserInterfaceInfoEntity selectByUserIdAndInterfaceId(Long userId, Long interfaceId) {
        LambdaQueryWrapper<UserInterfaceInfoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInterfaceInfoEntity::getUserId,userId)
                .eq(UserInterfaceInfoEntity::getInterfaceInfoId,interfaceId);
        return this.getOne(wrapper);
    }

    @Override
    public PageData<MyInterfacePageVO> myInterfacePage(Map<String, Object> params) {
        UserDetails user = UserContext.getUserInfo();

        LambdaQueryWrapper<UserInterfaceInfoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInterfaceInfoEntity::getUserId,user.getId())
                .orderByDesc(UserInterfaceInfoEntity::getCreateDate);

        IPage<UserInterfaceInfoEntity> iPage = baseMapper.selectPage(PageUtil.getPage(params), wrapper);
        List<MyInterfacePageVO> data = iPage.getRecords().stream().map(item -> {
            InterfaceInfoEntity interfaceInfo = interfaceService.getById(item.getInterfaceInfoId());
            MyInterfacePageVO vo = ConvertUtil.convert(item, MyInterfacePageVO.class);
            vo.setInterfaceName(interfaceInfo == null ? "" : interfaceInfo.getName());
            vo.setStatus(interfaceInfo == null ? InterfaceStatusEnum.OFFLINE.getValue() : interfaceInfo.getStatus());
            return vo;
        }).collect(Collectors.toList());
        return new PageData<>(data, iPage.getTotal());
    }

}
