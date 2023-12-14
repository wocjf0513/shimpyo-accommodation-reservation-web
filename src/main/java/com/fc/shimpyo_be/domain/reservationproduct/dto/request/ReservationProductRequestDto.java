package com.fc.shimpyo_be.domain.reservationproduct.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record ReservationProductRequestDto(
    @NotNull
    Long cartId,
    @NotNull
    Long roomId,
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "올바른 날짜 형식이 아닙니다.(yyyy-MM-dd 형식으로 입력하세요.)")
    String startDate,
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "올바른 날짜 형식이 아닙니다.(yyyy-MM-dd 형식으로 입력하세요.)")
    String endDate,
    String visitorName,
    String visitorPhone,
    @Min(value = 0, message = "객실 이용 금액은 0원 이상부터 가능합니다.")
    Integer price
) {
}
