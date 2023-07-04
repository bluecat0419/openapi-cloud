package com.zty.auth.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.zty.common.core.vo.UserDetails;
import com.zty.mvcconfig.context.UserContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 公共字段，自动填充值
 * @author admin
 */
@Component
public class FieldMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        UserDetails user = UserContext.getUserInfo();
        Date date = new Date();

        //创建者
        strictInsertFill(metaObject, "creator", Long.class, user == null ? null:user.getId());
        //创建时间
        strictInsertFill(metaObject, "createDate", Date.class, date);

        //更新者
        strictInsertFill(metaObject, "updater", Long.class, user == null ? null:user.getId());
        //更新时间
        strictInsertFill(metaObject, "updateDate", Date.class, date);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //更新者
        strictUpdateFill(metaObject, "updater", Long.class, UserContext.getUserInfo().getId());
        //更新时间
        strictUpdateFill(metaObject, "updateDate", Date.class, new Date());
    }
}
