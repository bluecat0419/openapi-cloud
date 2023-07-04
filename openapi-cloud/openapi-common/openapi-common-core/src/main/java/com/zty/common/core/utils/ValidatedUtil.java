package com.zty.common.core.utils;

import com.zty.common.core.exception.BusinessException;

import static com.zty.common.core.constant.Constant.*;

import java.util.Map;

/**
 * 校验工具类
 */
public class ValidatedUtil {

    /**
     * 校验分页参数
     * @param params
     */
    public static void checkLimit(Map<String,Object> params){
        Object limit = params.get(LIMIT);

        if(limit == null){
            return;
        }

        if(Integer.parseInt(limit.toString()) > MAX_LIMIT){
            throw new BusinessException("每页数量不能超过50");
        }
    }

}
