package com.zty.mvcconfig.advice;

import com.zty.common.core.exception.BusinessException;
import com.zty.common.core.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * @author ZhangTianYou
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    public CommonResponse<String> exceptionHandler(HttpServletRequest request, Exception e){
        log.error("url:[{}] , openapi server has error: [{}]",request.getRequestURI(),e.getMessage(),e);
        return new CommonResponse<>(500,"内部服务器异常");
    }

    /**
     * 自定义异常处理器
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    public CommonResponse<String> exceptionHandler(HttpServletRequest request, BusinessException e){
        return new CommonResponse<>(e.getCode(),e.getMsg());
    }

    /**
     * 参数校验异常处理器
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<?> handlerException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String errorMsg = bindingResult.getFieldErrors().stream().map(o -> o.getDefaultMessage()).collect(Collectors.joining(","));
        return new CommonResponse<>(500, errorMsg);
    }


}
