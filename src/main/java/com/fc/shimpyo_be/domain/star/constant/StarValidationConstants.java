package com.fc.shimpyo_be.domain.star.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StarValidationConstants {

    // validation constraint value
    public static final String SCORE_DECIMAL_MIN_VALUE = "0.0";
    public static final String SCORE_DECIMAL_MAX_VALUE = "5.0";
    public static final int SCORE_DIGITS_INTEGER_VALUE = 1;
    public static final int SCORE_DIGITS_FRACTION_VALUE = 1;

    // validation message
    public static final String STAR_PRODUCTID_NOTNULL_MESSAGE = "별점 등록 대상 숙소 아이디는 필수값입니다.";
    public static final String SCORE_DECIMAL_MIN_MESSAGE = "별점은 최소 0.0점 미만일 수 없습니다.";
    public static final String SCORE_DECIMAL_MAX_MESSAGE = "별점은 최대 5.0점을 초과할 수 없습니다.";
    public static final String SCORE_DIGITS_MESSAGE = "별점은 정수 1자리, 소수점 1자리까지만 가능합니다.";
}
