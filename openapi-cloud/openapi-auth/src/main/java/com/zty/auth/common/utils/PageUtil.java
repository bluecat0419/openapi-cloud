package com.zty.auth.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zty.common.core.constant.Constant;

import java.util.Map;

/**
 * @author ZhangTianYou
 * @date 2022/10/28
 */
public class PageUtil {

    /**
     * 获取分页对象
     * @param params
     * @return
     * @param <T>
     */
    public static <T> IPage<T> getPage(Map<String,Object> params){
        //默认当前页
        long currentPage=1;
        //默认总数量
        long limit=10;

        if(params.get(Constant.PAGE) != null){
            currentPage= Long.parseLong((String) params.get(Constant.PAGE));
        }
        if(params.get(Constant.LIMIT) != null){
            limit= Long.parseLong((String) params.get(Constant.LIMIT));
        }

        IPage<T> page=new Page<>(currentPage,limit);

        return page;

    }

}
