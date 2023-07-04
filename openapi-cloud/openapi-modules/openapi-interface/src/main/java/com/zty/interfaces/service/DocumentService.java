package com.zty.interfaces.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zty.common.core.page.PageData;
import com.zty.interfaces.entity.DocumentEntity;
import com.zty.interfaces.request.SaveOrUpdateDocumentRequest;
import com.zty.interfaces.vo.DocumentTitleVO;
import com.zty.interfaces.vo.DocumentVO;

import java.util.List;
import java.util.Map;

/**
 * 系统文档信息
 * @author ZhangTianYou
 */
public interface DocumentService extends IService<DocumentEntity> {

    /**
     * 分页
     * @param params
     * @return
     */
    PageData<DocumentVO> page(Map<String,Object> params);

    /**
     * 详情
     * @param id
     * @return
     */
    DocumentVO get(Long id);

    /**
     * 查询所有系统文档信息标题集合
     * @return
     */
    List<DocumentTitleVO> titleList();

    /**
     * 保存
     * @param request
     * @return
     */
    Long save(SaveOrUpdateDocumentRequest request);

    /**
     * 修改
     * @param request
     * @return
     */
    Boolean update(SaveOrUpdateDocumentRequest request);

    /**
     * 逻辑删除
     * @param id
     * @return
     */
    Boolean fakeDelete(Long id);
}
