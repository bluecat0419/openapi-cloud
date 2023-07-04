package com.zty.interfaces.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zty.interfaces.entity.DocumentEntity;
import com.zty.interfaces.vo.DocumentTitleVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统文档信息
 * @author ZhangTianYou
 */
@Mapper
public interface DocumentMapper extends BaseMapper<DocumentEntity> {

    /**
     * 查询所有系统文档信息标题集合
     * @return
     */
    List<DocumentTitleVO> titleList();

}
