package com.zty.interfaces.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zty.common.core.constant.Constant;
import com.zty.common.core.constant.InterfaceStatusEnum;
import com.zty.common.core.constant.IsDeletedEnum;
import com.zty.common.core.exception.BusinessException;
import com.zty.common.core.utils.SpringUtils;
import com.zty.common.redis.keys.RedisKeys;
import com.zty.interfaces.common.utils.PageUtil;
import com.zty.interfaces.entity.InterfaceInfoEntity;
import com.zty.interfaces.mapper.InterfaceInfoMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 排序类型处理器
 * @author ZhangTianYou
 */
@SuppressWarnings("all")
public enum SortTypeHandler {
    /**
     * 最新
     */
    latest(0){
        @Override
        public QueryDTO query(Map<String,Object> params) {
            //接口名称
            String name = (String) params.get("name");

            LambdaQueryWrapper<InterfaceInfoEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(InterfaceInfoEntity::getIsDeleted, IsDeletedEnum.NOT_DELETED.getValue())
                    .eq(InterfaceInfoEntity::getStatus, InterfaceStatusEnum.ONLINE.getValue())
                    .like(StringUtils.isNotBlank(name),InterfaceInfoEntity::getName,name)
                    .orderByDesc(InterfaceInfoEntity::getCreateDate);

            IPage<InterfaceInfoEntity> page = interfaceInfoMapper.selectPage(PageUtil.getPage(params), wrapper);
            List<Long> list = page.getRecords().stream().map(item -> {
                return item.getId();
            }).collect(Collectors.toList());
            return new QueryDTO(list,page.getTotal());
        }
    },
    /**
     * 评分
     */
    score(1){
        @Override
        public QueryDTO query(Map<String,Object> params) {
            //接口名称
            String name = (String) params.get("name");
            //当前页
            long currentPage= Long.parseLong((String) params.get(Constant.PAGE));
            //每页显示条数
            long limit= Long.parseLong((String) params.get(Constant.LIMIT));

            LambdaQueryWrapper<InterfaceInfoEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(InterfaceInfoEntity::getIsDeleted,IsDeletedEnum.NOT_DELETED.getValue())
                    .eq(InterfaceInfoEntity::getStatus,InterfaceStatusEnum.ONLINE.getValue())
                    .like(StringUtils.isNotBlank(name),InterfaceInfoEntity::getName,name);
            List<InterfaceInfoEntity> list = interfaceInfoMapper.selectList(wrapper);

            List<Long> ids = list.stream().map(item -> {
                return item.getId();
            }).sorted((a, b) -> {
                Double scoreA = redisTemplate.opsForZSet().score(RedisKeys.getApiScoreKey(), a.toString());
                Double scoreB = redisTemplate.opsForZSet().score(RedisKeys.getApiScoreKey(), b.toString());
                return scoreB.compareTo(scoreA);
            }).skip((currentPage - 1) * limit).limit(limit).collect(Collectors.toList());
            return new QueryDTO(ids, (long) list.size());
        }
    },
    /**
     * 成交量
     */
    dealCount(2){
        @Override
        public QueryDTO query(Map<String,Object> params) {
            //接口名称
            String name = (String) params.get("name");
            //当前页
            long currentPage= Long.parseLong((String) params.get(Constant.PAGE));
            //每页显示条数
            long limit= Long.parseLong((String) params.get(Constant.LIMIT));

            LambdaQueryWrapper<InterfaceInfoEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(InterfaceInfoEntity::getIsDeleted,IsDeletedEnum.NOT_DELETED.getValue())
                    .eq(InterfaceInfoEntity::getStatus,InterfaceStatusEnum.ONLINE.getValue())
                    .like(StringUtils.isNotBlank(name),InterfaceInfoEntity::getName,name);
            List<InterfaceInfoEntity> list = interfaceInfoMapper.selectList(wrapper);

            List<Long> ids = list.stream().map(item -> {
                return item.getId();
            }).sorted((a, b) -> {
                Double scoreA = redisTemplate.opsForZSet().score(RedisKeys.getApiDealCountKey(), a.toString());
                Double scoreB = redisTemplate.opsForZSet().score(RedisKeys.getApiDealCountKey(), b.toString());
                return scoreB.compareTo(scoreA);
            }).skip((currentPage - 1) * limit).limit(limit).collect(Collectors.toList());
            return new QueryDTO(ids, (long) list.size());
        }
    }
    ;


    InterfaceInfoMapper interfaceInfoMapper = SpringUtils.getBean(InterfaceInfoMapper.class);
    RedisTemplate redisTemplate = SpringUtils.getBean("redisTemplate");

    /**
     * 排序类型
     */
    private int sortType;

    SortTypeHandler(int sortType) {
        this.sortType = sortType;
    }

    public abstract QueryDTO query(Map<String,Object> params);

    /**
     * 获取处理器
     * @param sortType      排序类型
     * @return
     */
    public static SortTypeHandler getHandle(Integer sortType){
        SortTypeHandler[] list = SortTypeHandler.values();
        for (SortTypeHandler data : list) {
            if(data.sortType==sortType){
                return data;
            }
        }
        throw new BusinessException("暂不支持当前业务类型,请重试或联系管理员");
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryDTO{
        /**
         * id 集合
         */
        private List<Long> ids;
        /**
         * 总数
         */
        private Long total;
        /**
         * 拼接sql语句
         */
        private String sql;

        public QueryDTO(List<Long> ids, Long total) {
            if(ids.isEmpty()){
                //防止集合为空时,wrapper构造器查询出所有数据
                ids.add(0L);
                this.sql = "0";
            }
            this.ids = ids;
            this.total = total;

            StringBuffer sqlString = new StringBuffer();
            ids.forEach(item->{
                sqlString.append(item).append(",");
            });
            // 删除最后的逗号
            if(sqlString.length()>0){
                sqlString.setLength(sqlString.length()-1);
                this.sql = sqlString.toString();
            }
        }
    }

}
