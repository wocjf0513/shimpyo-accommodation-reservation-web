package com.fc.shimpyo_be.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 회원
    ALREADY_EXISTS_MEMBER(HttpStatus.BAD_REQUEST, "이미 가입된 회원 입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호 양식에 맞지 않습니다."),
    UNMATCHED_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token 이 유효하지 않습니다."),
    LOGGED_OUT(HttpStatus.UNAUTHORIZED, "로그아웃 된 회원 입니다."),
    UNMATCHED_MEMBER(HttpStatus.UNAUTHORIZED, "토큰의 회원 정보가 일치하지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다."),

    // 상품
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다."),
    ROOM_NOT_RESERVE(HttpStatus.FORBIDDEN, "예약 불가능한 방입니다."),
    ROON_NOT_FOUND(HttpStatus.NOT_FOUND,"방 정보를 찾을 수 없습니다."),


    // 장바구니
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니 정보를 찾을 수 없습니다."),
    CART_NOT_DELETE(HttpStatus.FORBIDDEN, "해당 장바구니를 삭제할 권한이 없습니다"),

    // Open API
    HTTP_CLIENT_CONNECTION_ERROR(HttpStatus.UNAUTHORIZED, "외부 API 연결에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String simpleMessage;

    ErrorCode(HttpStatus httpStatus, String simpleMessage) {
        this.httpStatus = httpStatus;
        this.simpleMessage = simpleMessage;
    }
}