package com.zty.openapi.sdk.common.exception;

public class OpenApiSDKException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private String requestId;
    private String errorCode;

    public OpenApiSDKException(String message) {
        this(message, "");
    }

    public OpenApiSDKException(String message, String requestId) {
        this(message, requestId, "");
    }

    public OpenApiSDKException(String message, String requestId, String errorCode) {
        super(message);
        this.requestId = requestId;
        this.errorCode = errorCode;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String toString() {
        return "[OpenApiSDKException]code: " + this.getErrorCode() + " message:" + this.getMessage() + " requestId:" + this.getRequestId();
    }

}
