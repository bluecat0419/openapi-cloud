package com.zty.mvcconfig.advice;

import com.zty.common.core.vo.CommonResponse;
import com.zty.mvcconfig.annotation.IgnoreResponseAdvice;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 实现统一响应
 */
@RestControllerAdvice("com.zty")
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 是否需要对响应进行处理
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        //判断获取到的Controller类是否标识了IgnoreResponseAdvice注解
        if(methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)){
            //那么就不需要对响应进行处理
            return false;
        }

        //同上,只不过是对方法进行判断
        if(methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class)){
            return false;
        }

        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        //定义最终的返回对象,默认返回正确响应
        CommonResponse<Object> response=new CommonResponse<>(0,"");

        if(null == o){
            //如果返回数据为null,则不进行包装
            return response;
        }else if (o instanceof CommonResponse){
            //如果包装类型为CommonResponse也不进行包装
            response= (CommonResponse<Object>) o;
        }else {
            //如果返回类型不为以上类型,则进行包装
            response.setData(o);
        }

        return response;
    }
}
