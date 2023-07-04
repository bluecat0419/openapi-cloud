package com.zty.common.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 对象转换工具类
 */
@Slf4j
public class ConvertUtil {

    public static <T> T convert(Object source,Class<T> target){
        if(source==null){
            return null;
        }

        T obj = null;

        try {
            obj=target.newInstance();
            BeanUtils.copyProperties(source,obj);
        } catch (Exception e) {
            log.error("对象转换失败,source:[{}],target:[{}],message:[{}]",source,target,e.getMessage(),e);
        }

        return obj;
    }

    public static <T> List<T> convert(Collection<?> sources, Class<T> target){
        if(sources==null){
            return null;
        }

        List targets=new ArrayList(sources.size());

        try {
            for (Object source : sources) {
                T targetObject = target.newInstance();
                BeanUtils.copyProperties(source,targetObject);
                targets.add(targetObject);
            }
        }catch (Exception e){
            log.error("转换异常:{}",e);
        }

        return targets;
    }

}
