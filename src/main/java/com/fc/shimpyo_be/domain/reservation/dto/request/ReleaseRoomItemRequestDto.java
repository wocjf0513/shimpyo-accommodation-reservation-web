package com.fc.shimpyo_be.domain.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import static com.fc.shimpyo_be.domain.reservation.constant.ReservationValidationConstants.*;

@Builder
public record ReleaseRoomItemRequestDto(
    @NotNull
    Long roomId,
    @Pattern(regexp = DATE_REGEX, message = "올바른 날짜 형식이 아닙니다.(yyyy-MM-dd 형식으로 입력하세요.)")
    String startDate,
    @Pattern(regexp = DATE_REGEX, message = "올바른 날짜 형식이 아닙니다.(yyyy-MM-dd 형식으로 입력하세요.)")
    String endDate
) {
}
