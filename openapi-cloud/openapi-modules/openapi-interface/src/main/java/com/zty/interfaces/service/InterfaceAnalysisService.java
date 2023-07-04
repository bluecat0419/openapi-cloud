package com.zty.interfaces.service;

import com.zty.interfaces.vo.InvokeCountAnalysisVO;

import java.util.List;

/**
 * 接口分析
 * @author ZhangTianYou
 */
public interface InterfaceAnalysisService {

    /**
     * 接口调用次数分析
     * @param topOrTail     top-前10   tail-后10
     * @return
     */
    List<InvokeCountAnalysisVO> invokeCountAnalysis(String topOrTail);

}
