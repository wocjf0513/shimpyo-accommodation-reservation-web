package com.fc.shimpyo_be.domain.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import static com.fc.shimpyo_be.domain.reservation.constant.ReservationValidationConstants.*;

@Builder
public record ReleaseRoomItemRequestDto(
    @NotNull
    Long roomId,
    @Pattern(regexp = DATE_REGEX, message = DATE_PATTERN_MESSAGE)
    String startDate,
    @Pattern(regexp = DATE_REGEX, message = DATE_PATTERN_MESSAGE)
    String endDate
) {
}
