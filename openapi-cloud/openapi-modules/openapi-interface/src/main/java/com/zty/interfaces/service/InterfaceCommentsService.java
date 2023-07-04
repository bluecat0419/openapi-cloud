package com.zty.interfaces.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zty.common.core.page.PageData;
import com.zty.interfaces.entity.InterfaceCommentsEntity;
import com.zty.interfaces.request.CommentRequest;
import com.zty.interfaces.vo.InterfaceCommentVO;

import java.util.Map;

/**
 * 接口评论
 * @author ZhangTianYou
 */
public interface InterfaceCommentsService extends IService<InterfaceCommentsEntity> {

    /**
     * 检查是否评论过
     * @param userId            用户id
     * @param interfaceInfoId   接口id
     * @return      true:评论过    false:未评论过
     */
    boolean checkComment(Long userId,Long interfaceInfoId);

    /**
     * 评论
     * @param request
     * @return
     */
    Long comment(CommentRequest request);

    /**
     * 分页查询
     * @param params
     * @return
     */
    PageData<InterfaceCommentVO> page(Map<String,Object> params);

}
