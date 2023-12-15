package com.fc.shimpyo_be.domain.reservation.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationValidationConstants {

    // regex
    public static final String DATE_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";
    public static final int RESERVATION_REQ_MIN_SIZE = 1;
    public static final int RESERVATION_REQ_MAX_SIZE = 3;
    public static final int TOTAL_PRICE_MIN_VALUE = 0;

    // validation message
    public static final String DATE_PATTERN_MESSAGE = "올바른 날짜 형식이 아닙니다.(yyyy-MM-dd 형식으로 입력하세요.)";
    public static final String RESERVATION_REQ_SIZE_MESSAGE = "최소 1개, 최대 3개의 객실 예약이 가능합니다.";
    public static final String PAYMETHOD_NOTNULL_MESSAGE = "null 일 수 없습니다. 정해진 결제 수단에서 선택하세요.";
    public static final String TOTAL_PRICE_MIN_MESSAGE = "총 결제 금액은 최소 0원 이상이어야 합니다.";
}
