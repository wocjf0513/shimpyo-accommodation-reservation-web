package com.fc.shimpyo_be.domain.reservation.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum PayMethod {

    CREDIT_CARD,
    KAKAO_PAY,
    TOSS_PAY,
    NAVER_PAY,
    PAYPAL;

    @JsonCreator
    public static PayMethod parse(String inputValue) {
        return Stream.of(PayMethod.values())
            .filter(t -> t.toString().equals(inputValue.toUpperCase()))
            .findFirst()
            .orElse(null);
    }
}
