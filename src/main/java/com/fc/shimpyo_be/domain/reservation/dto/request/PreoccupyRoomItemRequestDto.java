package com.fc.shimpyo_be.domain.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record PreoccupyRoomItemRequestDto(
    @NotNull
    Long cartId,
    @NotNull
    Long roomCode,
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "올바른 날짜 형식이 아닙니다.(yyyy-MM-dd 형식으로 입력하세요.)")
    String startDate,
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "올바른 날짜 형식이 아닙니다.(yyyy-MM-dd 형식으로 입력하세요.)")
    String endDate
) {
}
