package com.fc.shimpyo_be.domain.reservation.dto.response;

import com.fc.shimpyo_be.domain.reservation.dto.request.SaveReservationRequestDto;
import com.fc.shimpyo_be.domain.reservation.entity.PayMethod;
import com.fc.shimpyo_be.domain.reservationproduct.dto.response.ReservationProductResponseDto;

import java.util.List;

public record SaveReservationResponseDto(
    Long reservationId,
    List<ReservationProductResponseDto> reservationProducts,
    PayMethod payMethod,
    Integer totalPrice
) {
    public SaveReservationResponseDto(Long reservationId, SaveReservationRequestDto requestDto) {
        this(
            reservationId,
            requestDto.reservationProducts()
                .stream()
                .map(ReservationProductResponseDto::new).toList(),
            requestDto.payMethod(),
            requestDto.totalPrice()
        );
    }
}
