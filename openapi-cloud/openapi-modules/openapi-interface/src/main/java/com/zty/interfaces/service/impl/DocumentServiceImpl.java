package com.zty.interfaces.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zty.common.core.constant.IsDeletedEnum;
import com.zty.common.core.page.PageData;
import com.zty.common.core.utils.ConvertUtil;
import com.zty.common.core.utils.ValidatedUtil;
import com.zty.interfaces.common.utils.PageUtil;
import com.zty.interfaces.entity.DocumentEntity;
import com.zty.interfaces.mapper.DocumentMapper;
import com.zty.interfaces.request.SaveOrUpdateDocumentRequest;
import com.zty.interfaces.service.DocumentService;
import com.zty.interfaces.vo.DocumentTitleVO;
import com.zty.interfaces.vo.DocumentVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 系统文档信息
 * @author ZhangTianYou
 */
@Slf4j
@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, DocumentEntity> implements DocumentService {

    @Resource
    DocumentMapper documentMapper;

    /**
     * 构建查询条件
     * @param params
     * @return
     */
    private LambdaQueryWrapper<DocumentEntity> buildWrapper(Map<String,Object> params){
        String title = (String) params.get("title");

        LambdaQueryWrapper<DocumentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(title),DocumentEntity::getTitle,title)
                .orderByAsc(DocumentEntity::getSort)
                .eq(DocumentEntity::getIsDeleted, IsDeletedEnum.NOT_DELETED.getValue());

        return wrapper;
    }

    @Override
    public PageData<DocumentVO> page(Map<String, Object> params) {
        ValidatedUtil.checkLimit(params);

        IPage<DocumentEntity> page = baseMapper.selectPage(PageUtil.getPage(params), buildWrapper(params));
        return new PageData<>(ConvertUtil.convert(page.getRecords(), DocumentVO.class),page.getTotal());
    }

    @Override
    public DocumentVO get(Long id) {
        DocumentEntity entity = this.getById(id);
        return ConvertUtil.convert(entity,DocumentVO.class);
    }

    @Override
    public List<DocumentTitleVO> titleList() {
        return documentMapper.titleList();
    }

    @Override
    public Long save(SaveOrUpdateDocumentRequest request) {
        DocumentEntity entity = ConvertUtil.convert(request, DocumentEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Boolean update(SaveOrUpdateDocumentRequest request) {
        DocumentEntity entity = ConvertUtil.convert(request, DocumentEntity.class);
        return this.updateById(entity);
    }

    @Override
    public Boolean fakeDelete(Long id) {
        DocumentEntity entity = new DocumentEntity();
        entity.setId(id);
        entity.setIsDeleted(IsDeletedEnum.DELETED.getValue());
        return this.updateById(entity);
    }

}
