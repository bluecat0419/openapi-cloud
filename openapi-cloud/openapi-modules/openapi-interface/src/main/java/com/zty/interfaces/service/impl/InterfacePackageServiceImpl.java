package com.zty.interfaces.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zty.common.core.page.PageData;
import com.zty.common.core.utils.ConvertUtil;
import com.zty.common.redis.keys.RedisKeys;
import com.zty.interfaces.common.entity.BaseEntity;
import com.zty.interfaces.common.utils.PageUtil;
import com.zty.interfaces.entity.InterfaceInfoEntity;
import com.zty.interfaces.entity.InterfacePackageEntity;
import com.zty.interfaces.mapper.InterfacePackageMapper;
import com.zty.interfaces.request.SaveOrUpdateInterfacePackageRequest;
import com.zty.interfaces.service.InterfacePackageService;
import com.zty.interfaces.vo.InterfacePackageDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 接口套餐
 * @author ZhangTianYou
 */
@Service
public class InterfacePackageServiceImpl extends ServiceImpl<InterfacePackageMapper, InterfacePackageEntity> implements InterfacePackageService {

    @Resource
    RedisTemplate redisTemplate;

    private LambdaQueryWrapper<InterfacePackageEntity> buildWrapper(Map<String,Object> params){
        Object interfaceInfoId = params.get("interfaceInfoId");
        Object status = params.get("status");
        String packageName = (String) params.get("packageName");

        LambdaQueryWrapper<InterfacePackageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(packageName),InterfacePackageEntity::getName,packageName)
                .orderByDesc(InterfacePackageEntity::getCreateDate);
        if(interfaceInfoId != null){
            wrapper.eq(InterfacePackageEntity::getInterfaceInfoId,Long.parseLong(interfaceInfoId.toString()));
        }
        if(status != null){
            wrapper.eq(InterfacePackageEntity::getStatus,Integer.parseInt(status.toString()));
        }
        return wrapper;
    }

    @Override
    public PageData<InterfacePackageEntity> page(Map<String, Object> params) {
        IPage<InterfacePackageEntity> iPage = baseMapper.selectPage(PageUtil.getPage(params), buildWrapper(params));
        return new PageData<>(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public InterfacePackageDetails details(Long id) {
        InterfacePackageDetails details = ConvertUtil.convert(this.getById(id), InterfacePackageDetails.class);

        InterfacePackageEntity obj = (InterfacePackageEntity) redisTemplate.opsForValue()
                .get(RedisKeys.getApiShowPriceKey(details.getInterfaceInfoId().toString()));
        details.setShowPrice(obj == null ? Boolean.FALSE : obj.getId().equals(id));
        return details;
    }

    @Override
    public Long save(SaveOrUpdateInterfacePackageRequest request) {
        InterfacePackageEntity entity = ConvertUtil.convert(request, InterfacePackageEntity.class);
        this.save(entity);

        if(request.getShowPrice()){
            setShowPrice(entity.getId());
        }
        return entity.getId();
    }

    @Override
    public Boolean update(SaveOrUpdateInterfacePackageRequest request) {
        InterfacePackageEntity entity = ConvertUtil.convert(request, InterfacePackageEntity.class);

        if(request.getShowPrice()){
            setShowPrice(entity.getId());
        }
        return this.updateById(entity);
    }

    @Override
    public Boolean setShowPrice(Long id) {
        InterfacePackageEntity entity = this.getById(id);

        if (entity != null) {
            redisTemplate.opsForValue().set(RedisKeys.getApiShowPriceKey(entity.getInterfaceInfoId().toString()),entity);
            return true;
        }
        return false;
    }

    @Override
    public List<InterfacePackageEntity> selectByInterfaceIdAndNotExpired(Long interfaceId) {
        LambdaQueryWrapper<InterfacePackageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterfacePackageEntity::getInterfaceInfoId,interfaceId)
                .eq(InterfacePackageEntity::getStatus,0)
                .and(and -> and.isNull(InterfacePackageEntity::getExpireDate)
                                .or()
                                .gt(InterfacePackageEntity::getExpireDate, new Date()));
        return this.list(wrapper);
    }
}
