package com.zty.interfaces.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zty.common.core.constant.InterfaceStatusEnum;
import com.zty.common.core.constant.IsDeletedEnum;
import com.zty.common.core.page.PageData;
import com.zty.common.core.utils.AssertUtil;
import com.zty.common.core.utils.ConvertUtil;
import com.zty.common.core.utils.ValidatedUtil;
import com.zty.common.redis.keys.RedisKeys;
import com.zty.interfaces.common.utils.PageUtil;
import com.zty.interfaces.entity.InterfaceInfoEntity;
import com.zty.interfaces.entity.InterfacePackageEntity;
import com.zty.interfaces.handler.SortTypeHandler;
import com.zty.interfaces.mapper.InterfaceInfoMapper;
import com.zty.interfaces.request.AddRegisterWelfareRequest;
import com.zty.interfaces.request.SaveOrUpdateInterfaceRequest;
import com.zty.interfaces.service.InterfacePackageService;
import com.zty.interfaces.service.InterfaceService;
import com.zty.interfaces.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ZhangTianYou
 */
@Slf4j
@Service
public class InterfaceServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfoEntity> implements InterfaceService {

    @Resource
    RedisTemplate redisTemplate;
    @Resource
    InterfacePackageService packageService;

    /**
     * 构建查询条件
     * @param params
     * @return
     */
    private LambdaQueryWrapper<InterfaceInfoEntity> buildWrapper(Map<String,Object> params){
        String name = (String) params.get("name");
        String url = (String) params.get("url");
        String method = (String) params.get("method");
        String responseType = (String) params.get("responseType");
        Object status = params.get("status");

        LambdaQueryWrapper<InterfaceInfoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name),InterfaceInfoEntity::getName,name)
                .like(StringUtils.isNotBlank(url),InterfaceInfoEntity::getUrl,url)
                .eq(StringUtils.isNotBlank(method),InterfaceInfoEntity::getMethod,method)
                .eq(StringUtils.isNotBlank(responseType),InterfaceInfoEntity::getResponseType,responseType)
                .eq(InterfaceInfoEntity::getIsDeleted, IsDeletedEnum.NOT_DELETED.getValue());
        if(status != null){
            wrapper.eq(InterfaceInfoEntity::getStatus, Integer.parseInt(status.toString()));
        }

        return wrapper;
    }

    @Override
    public PageData<InterfaceInfoVO> page(Map<String, Object> params) {
        ValidatedUtil.checkLimit(params);

        IPage<InterfaceInfoEntity> page = baseMapper.selectPage(PageUtil.getPage(params), buildWrapper(params));
        return new PageData<>(ConvertUtil.convert(page.getRecords(),InterfaceInfoVO.class),page.getTotal());
    }

    @Override
    public InterfaceInfoDetailsVO get(Long id) {
        InterfaceInfoEntity entity = this.getById(id);
        InterfaceInfoDetailsVO details = ConvertUtil.convert(entity,InterfaceInfoDetailsVO.class);

        details.setRequestParamList(JSON.parseArray(entity.getRequestParams(), InterfaceDocVO.Param.class));
        details.setRequestBodyList(JSON.parseArray(entity.getRequestBody(), InterfaceDocVO.Param.class));
        details.setRequestHeaderList(JSON.parseArray(entity.getRequestHeader(), InterfaceDocVO.Param.class));

        Double invokeCount = redisTemplate.opsForZSet().score(RedisKeys.getApiInvokeCountKey(), entity.getId());
        details.setInvokeCount(invokeCount == null ? 0:invokeCount.intValue());
        return details;
    }

    @Override
    public Long save(SaveOrUpdateInterfaceRequest request) {
        InterfaceInfoEntity entity = ConvertUtil.convert(request, InterfaceInfoEntity.class);
        entity.setRequestParams(CollectionUtils.isEmpty(request.getRequestParamList()) ? "":JSON.toJSONString(request.getRequestParamList()));
        entity.setRequestHeader(CollectionUtils.isEmpty(request.getRequestHeaderList()) ? "":JSON.toJSONString(request.getRequestHeaderList()));
        entity.setRequestBody(CollectionUtils.isEmpty(request.getRequestBodyList()) ? "":JSON.toJSONString(request.getRequestBodyList()));
        this.save(entity);
        //初始化评分
        redisTemplate.opsForZSet().add(RedisKeys.getApiScoreKey(),entity.getId().toString(),0.0);
        //初始化成交量
        redisTemplate.opsForZSet().add(RedisKeys.getApiDealCountKey(),entity.getId().toString(),0);
        return entity.getId();
    }

    @Override
    public Boolean update(SaveOrUpdateInterfaceRequest request) {
        InterfaceInfoEntity entity = ConvertUtil.convert(request, InterfaceInfoEntity.class);
        entity.setRequestParams(CollectionUtils.isEmpty(request.getRequestParamList()) ? "":JSON.toJSONString(request.getRequestParamList()));
        entity.setRequestHeader(CollectionUtils.isEmpty(request.getRequestHeaderList()) ? "":JSON.toJSONString(request.getRequestHeaderList()));
        entity.setRequestBody(CollectionUtils.isEmpty(request.getRequestBodyList()) ? "":JSON.toJSONString(request.getRequestBodyList()));

        return this.updateById(entity);
    }

    @Override
    public boolean fakeDelete(Long id) {
        LambdaUpdateWrapper<InterfaceInfoEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(InterfaceInfoEntity::getId,id)
                .set(InterfaceInfoEntity::getIsDeleted,IsDeletedEnum.DELETED.getValue());
        return this.update(wrapper);
    }

    @Override
    public boolean addRegisterWelfare(AddRegisterWelfareRequest request) {
        Long add = redisTemplate.opsForSet().add(RedisKeys.getRegisterWelfareKey(), request);
        return add != 0;
    }

    @Override
    public PageData<InterfaceHomePageVO> homepage(Map<String, Object> params) {
        ValidatedUtil.checkLimit(params);
        //排序类型  0:最新  1:评分  2:成交量
        Integer sortType = Integer.parseInt(Optional.of(params.get("sortType")).orElse(0).toString());

        SortTypeHandler handle = SortTypeHandler.getHandle(sortType);
        SortTypeHandler.QueryDTO query = handle.query(params);

        LambdaQueryWrapper<InterfaceInfoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(InterfaceInfoEntity::getId,query.getIds())
                .last(String.format("ORDER BY FIELD(id, %s)",query.getSql()));
        List<InterfaceInfoEntity> list = baseMapper.selectList(wrapper);

        List<InterfaceHomePageVO> data = list.stream().map(item -> {
            InterfaceHomePageVO vo = new InterfaceHomePageVO();
            vo.setId(item.getId());
            vo.setName(item.getName());
            vo.setIcon(item.getIcon());
            vo.setDescription(item.getDescription());
            InterfacePackageEntity packageInfo = (InterfacePackageEntity) redisTemplate.opsForValue().get(RedisKeys.getApiShowPriceKey(item.getId().toString()));
            vo.setPrice(Objects.isNull(packageInfo) ? "¥-" : String.format("¥%s/%s次", packageInfo.getPrice(),packageInfo.getInvokeCount()));
            vo.setScore(redisTemplate.opsForZSet().score(RedisKeys.getApiScoreKey(), item.getId().toString()));
            vo.setDealCount(redisTemplate.opsForZSet().score(RedisKeys.getApiDealCountKey(), item.getId().toString()).intValue());
            return vo;
        }).collect(Collectors.toList());

        return new PageData<>(data,query.getTotal());
    }

    @Override
    public InterfaceHomeDetailVO details(Long id) {
        LambdaQueryWrapper<InterfaceInfoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterfaceInfoEntity::getId,id)
                .eq(InterfaceInfoEntity::getStatus,InterfaceStatusEnum.ONLINE.getValue())
                .eq(InterfaceInfoEntity::getIsDeleted,IsDeletedEnum.NOT_DELETED.getValue());
        InterfaceInfoEntity entity = baseMapper.selectOne(wrapper);
        AssertUtil.isTrue(Objects.isNull(entity),"API不存在或已下线,请重试");

        InterfaceHomeDetailVO detail = new InterfaceHomeDetailVO();
        detail.setId(entity.getId());
        detail.setName(entity.getName());
        detail.setIcon(entity.getIcon());
        detail.setDescription(entity.getDescription());
        //todo 外层的 price 用不到，后续删除
        InterfacePackageEntity packageInfo = (InterfacePackageEntity) redisTemplate.opsForValue().get(RedisKeys.getApiShowPriceKey(entity.getId().toString()));
        detail.setPrice(Objects.isNull(packageInfo) ? "¥-" : String.format("¥%s/%s次", packageInfo.getPrice(),packageInfo.getInvokeCount()));
        detail.setScore(redisTemplate.opsForZSet().score(RedisKeys.getApiScoreKey(), entity.getId().toString()));
        detail.setDealCount(redisTemplate.opsForZSet().score(RedisKeys.getApiDealCountKey(), entity.getId().toString()).intValue());

        List<InterfacePackageEntity> packageList = packageService.selectByInterfaceIdAndNotExpired(entity.getId());
        List<InterfaceHomeDetailVO.PackageVO> packages = packageList.stream().map(item -> {
            InterfaceHomeDetailVO.PackageVO vo = new InterfaceHomeDetailVO.PackageVO();
            vo.setPackageId(item.getId());
            vo.setPackageName(item.getName());
            vo.setPrice(String.format("¥%s/%s次", item.getPrice(),item.getInvokeCount()));
            return vo;
        }).collect(Collectors.toList());
        detail.setPackages(packages);

        return detail;
    }

    @Override
    public InterfaceDocVO interfaceDoc(Long id) {
        LambdaQueryWrapper<InterfaceInfoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterfaceInfoEntity::getIsDeleted,IsDeletedEnum.NOT_DELETED.getValue())
                .eq(InterfaceInfoEntity::getId,id);
        InterfaceInfoEntity entity = baseMapper.selectOne(wrapper);
        AssertUtil.isTrue(entity == null,"API不存在,请重试");

        InterfaceDocVO doc = ConvertUtil.convert(entity,InterfaceDocVO.class);
        doc.setRequestParamsList(JSON.parseArray(entity.getRequestParams(), InterfaceDocVO.Param.class));
        doc.setRequestBodyList(JSON.parseArray(entity.getRequestBody(), InterfaceDocVO.Param.class));
        doc.setRequestHeaderList(JSON.parseArray(entity.getRequestHeader(), InterfaceDocVO.Param.class));
        return doc;
    }

}
