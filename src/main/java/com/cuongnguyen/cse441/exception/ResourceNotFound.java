package com.cuongnguyen.cse441.exception;

public class ResourceNotFound extends RuntimeException{

    private ErrorCode errorCode;
    public ResourceNotFound( ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
