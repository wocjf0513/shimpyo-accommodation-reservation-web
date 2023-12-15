package com.fc.shimpyo_be.domain.reservationproduct.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import static com.fc.shimpyo_be.domain.reservationproduct.constant.ReservationProductValidationConstants.*;

@Builder
public record ReservationProductRequestDto(
    @NotNull
    Long cartId,
    @NotNull
    Long roomId,
    @Pattern(regexp = DATE_REGEX, message = DATE_PATTERN_MESSAGE)
    String startDate,
    @Pattern(regexp = DATE_REGEX, message = DATE_PATTERN_MESSAGE)
    String endDate,
    @NotBlank
    String visitorName,
    @NotBlank
    @Pattern(regexp = PHONE_NUMBER_REGEX, message = PHONE_NUMBER_PATTERN_MESSAGE)
    String visitorPhone,
    @Min(value = PRICE_MIN_VALUE, message = PRICE_MIN_MESSAGE)
    Integer price
) {
}
