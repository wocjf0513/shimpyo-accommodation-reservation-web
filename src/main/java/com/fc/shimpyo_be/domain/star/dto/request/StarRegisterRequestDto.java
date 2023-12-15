package com.fc.shimpyo_be.domain.star.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import static com.fc.shimpyo_be.domain.star.constant.StarValidationConstants.*;

public record StarRegisterRequestDto(
    @NotNull
    Long reservationProductId,
    @NotNull(message = STAR_PRODUCTID_NOTNULL_MESSAGE)
    Long productId,
    @DecimalMax(value = SCORE_DECIMAL_MAX_VALUE, message = SCORE_DECIMAL_MAX_MESSAGE)
    @DecimalMin(value = SCORE_DECIMAL_MIN_VALUE, message = SCORE_DECIMAL_MIN_MESSAGE)
    @Digits(integer = SCORE_DIGITS_INTEGER_VALUE, fraction = SCORE_DIGITS_FRACTION_VALUE,
        message = SCORE_DIGITS_MESSAGE)
    float score
) {
}
