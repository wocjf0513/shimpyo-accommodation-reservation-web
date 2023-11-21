package com.fc.shimpyo_be.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 상품
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다."),

    // Open API
    HTTP_CLIENT_CONNECTION_ERROR(HttpStatus.UNAUTHORIZED, "외부 API 연결에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String simpleMessage;

    ErrorCode(HttpStatus httpStatus, String simpleMessage) {
        this.httpStatus = httpStatus;
        this.simpleMessage = simpleMessage;
    }
}