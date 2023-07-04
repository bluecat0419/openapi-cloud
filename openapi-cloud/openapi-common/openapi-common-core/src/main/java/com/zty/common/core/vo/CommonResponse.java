package com.zty.common.core.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <h1>通用响应对象</h1>
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> implements Serializable {

    /**
     * 响应码
     */
    private Integer code;
    /**
     * 响应信息
     */
    private String msg;
    /**
     * 响应数据
     */
    private T data;

    public CommonResponse(Integer code, String message){
        this.code=code;
        this.msg=message;
    }

    public CommonResponse<T> ok(){
        this.code=0;
        return this;
    }

    public CommonResponse<T> ok(T data){
        this.code=0;
        this.data=data;
        return this;
    }

    public CommonResponse<T> error(){
        this.code=500;
        this.msg="服务器内部异常";
        return this;
    }

    public CommonResponse<T> error(int code,String msg){
        this.code=code;
        this.msg=msg;
        return this;
    }

}
