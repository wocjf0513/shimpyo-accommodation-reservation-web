package com.fc.shimpyo_be.domain.star.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public record StarRegisterRequestDto(
    @NotNull(message = "별점 등록 회원 아이디는 필수값입니다.")
    Long memberId,
    @NotNull(message = "별점 등록 대상 숙소 아이디는 필수값입니다.")
    Long productId,
    @DecimalMax(value = "5.0", message = "별점은 최대 5.0점을 초과할 수 없습니다.")
    @DecimalMin(value = "0.0", message = "별점은 최소 0.0점 미만일 수 없습니다.")
    @Digits(integer = 1, fraction = 1, message = "별점은 정수 1자리, 소수점 1자리까지만 가능합니다.")
    float score
) {
}
