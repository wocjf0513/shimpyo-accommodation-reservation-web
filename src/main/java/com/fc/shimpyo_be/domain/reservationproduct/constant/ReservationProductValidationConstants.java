package com.fc.shimpyo_be.domain.reservationproduct.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationProductValidationConstants {

    // validation constraint value
    public static final String DATE_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";
    public static final String PHONE_NUMBER_REGEX = "^01(?:0|1|[6-9])-\\d{4}-\\d{4}$";
    public static final int PRICE_MIN_VALUE = 0;

    // validation message
    public static final String DATE_PATTERN_MESSAGE = "올바른 날짜 형식이 아닙니다.(yyyy-MM-dd 형식으로 입력하세요.)";
    public static final String PHONE_NUMBER_PATTERN_MESSAGE = "올바른 휴대전화 번호를 입력하세요.('-' 포함)";
    public static final String PRICE_MIN_MESSAGE = "객실 이용 금액은 0원 이상부터 가능합니다.";
}
