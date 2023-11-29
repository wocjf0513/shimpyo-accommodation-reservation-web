package com.fc.shimpyo_be.domain.reservation.dto.request;

import com.fc.shimpyo_be.domain.reservation.entity.PayMethod;
import com.fc.shimpyo_be.domain.reservationproduct.dto.request.ReservationProductRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SaveReservationRequestDto(
    @Valid
    @Size(min = 1, max = 3, message = "최소 1개, 최대 3개의 객실 예약이 가능합니다.")
    List<ReservationProductRequestDto> reservationProducts,
    @NotNull(message = "null 일 수 없습니다. 정해진 결제 수단에서 선택하세요.")
    PayMethod payMethod,
    @Min(value = 0, message = "총 결제 금액은 음수일 수 없습니다.")
    Integer totalPrice
) {
}
