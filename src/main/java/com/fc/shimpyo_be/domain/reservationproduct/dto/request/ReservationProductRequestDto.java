package com.fc.shimpyo_be.domain.reservationproduct.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ReservationProductRequestDto(
    @NotNull
    Long roomId,
    @NotBlank
    String productName,
    @NotBlank
    String roomName,
    @NotNull
    Integer standard,
    @NotNull
    Integer max,
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "올바른 날짜 형식이 아닙니다.(yyyy-MM-dd 형식으로 입력하세요.)")
    String startDate,
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "올바른 날짜 형식이 아닙니다.(yyyy-MM-dd 형식으로 입력하세요.)")
    String endDate,
    @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "올바른 시간 형식이 아닙니다.(HH:mm 형식으로 입력하세요.)")
    String checkIn,
    @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "올바른 시간 형식이 아닙니다.(HH:mm 형식으로 입력하세요.)")
    String checkOut,
    String visitorName,
    String visitorPhone,
    @Min(value = 0, message = "객실 이용 금액은 음수일 수 없습니다.")
    Integer price
) {
}
