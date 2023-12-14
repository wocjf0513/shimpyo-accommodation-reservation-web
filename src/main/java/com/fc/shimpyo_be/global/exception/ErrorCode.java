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

    // 숙소
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "숙소 정보를 찾을 수 없습니다."),
    ROOM_NOT_RESERVE(HttpStatus.FORBIDDEN, "예약 불가능한 방입니다."),
    ROON_NOT_FOUND(HttpStatus.NOT_FOUND, "방 정보를 찾을 수 없습니다."),

    // 예약
    UNAVAILABLE_ROOMS(HttpStatus.BAD_REQUEST, "예약 불가능한 방이 존재합니다."),
    LOCK_FAIL(HttpStatus.BAD_REQUEST, "요청 완료에 실패했습니다. 재시도가 필요합니다."),
    RESERVATION_VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "예약 선점 기한이 만료되었거나, 잘못된 예약 요청으로 예약이 불가합니다."),

    // 예약 숙소
    RESERVATION_PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "예약 숙소 정보를 찾을 수 없습니다."),
    FORBIDDEN_CANCEL_RESERVATION_PRODUCT(HttpStatus.FORBIDDEN, "예약 숙소을 취소할 권한이 없습니다."),

    // 장바구니
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니 정보를 찾을 수 없습니다."),
    CART_NOT_DELETE(HttpStatus.FORBIDDEN, "해당 장바구니를 삭제할 권한이 없습니다"),

    // 별점
    REGISTER_BEFORE_CHECKOUT(HttpStatus.BAD_REQUEST, "별점 등록은 체크아웃 이후에 가능합니다."),
    EXPIRED_STAR_REGISTER_DATE(HttpStatus.BAD_REQUEST, "별점 등록 가능 기간이 만료되었습니다.(체크아웃 후 2주 이내)"),

    // Open API
    HTTP_CLIENT_CONNECTION_ERROR(HttpStatus.UNAUTHORIZED, "외부 API 연결에 실패했습니다."),
    OPEN_API_ERROR(HttpStatus.NOT_FOUND, "오픈 API에서 데이터를 불러오는데 실패했습니다."),

    // Common
    INVALID_DATE(HttpStatus.BAD_REQUEST,"잘못된 날짜 데이터입니다."),

    // 즐겨찾기
    FAVORITE_ALREADY_REGISTER(HttpStatus.BAD_REQUEST, "이미 즐겨찾기에 등록한 숙소입니다."),
    FAVORITE_NOT_FOUND(HttpStatus.NOT_FOUND, "즐겨찾기 정보를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String simpleMessage;

    ErrorCode(HttpStatus httpStatus, String simpleMessage) {
        this.httpStatus = httpStatus;
        this.simpleMessage = simpleMessage;
    }
}