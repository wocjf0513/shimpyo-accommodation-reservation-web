package com.fc.shimpyo_be.domain.reservation.dto.response;

import com.fc.shimpyo_be.domain.reservation.entity.PayMethod;
import com.fc.shimpyo_be.domain.reservationproduct.dto.response.ReservationProductResponseDto;
import lombok.Builder;

import java.util.List;

@Builder
public record SaveReservationResponseDto(
    Long reservationId,
    List<ReservationProductResponseDto> reservationProducts,
    PayMethod payMethod,
    Integer totalPrice,
    String createdAt
) {
}
