package com.fc.shimpyo_be.global.exception;

import lombok.Getter;

@Getter
public abstract class ApplicationException extends RuntimeException{

    private final ErrorCode errorCode;

    protected ApplicationException(ErrorCode errorCode) {
        super(errorCode.getSimpleMessage());
        this.errorCode = errorCode;
    }
}