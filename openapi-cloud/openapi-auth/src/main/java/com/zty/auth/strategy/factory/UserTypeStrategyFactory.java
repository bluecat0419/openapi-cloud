package com.zty.auth.strategy.factory;

import com.zty.auth.strategy.AccountStrategy;
import com.zty.auth.strategy.EmailStrategy;
import com.zty.auth.strategy.UserTypeStrategy;
import com.zty.auth.strategy.PhoneStrategy;
import com.zty.common.core.utils.SpringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户登录类型策略工厂
 * @author ZhangTianYou
 * @date 2022/10/26
 */
public class UserTypeStrategyFactory {

    private static Map<Integer, UserTypeStrategy> STRATEGY_MAP=new HashMap<>(5);

    static {
        STRATEGY_MAP.put(UserType.ACCOUNT, SpringUtils.getBean(AccountStrategy.class));
        STRATEGY_MAP.put(UserType.PHONE, SpringUtils.getBean(PhoneStrategy.class));
        STRATEGY_MAP.put(UserType.EMAIL, SpringUtils.getBean(EmailStrategy.class));
    }

    private UserTypeStrategyFactory() {
    }

    /**
     * 获取指定类型方法
     * @param type     登录类型
     * @return
     */
    public static UserTypeStrategy getStrategy(Integer type){
        return STRATEGY_MAP.get(type);
    }

    /**
     * 用户登录类型
     */
    private interface UserType{
        /**
         * 账号
         */
        Integer ACCOUNT=0;
        /**
         * 手机
         */
        Integer PHONE=1;
        /**
         * 邮箱
         */
        Integer EMAIL=2;
    }

}
