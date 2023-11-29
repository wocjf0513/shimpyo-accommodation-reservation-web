package com.fc.shimpyo_be.domain.reservationproduct.dto.response;

import com.fc.shimpyo_be.domain.reservationproduct.dto.request.ReservationProductRequestDto;

public record ReservationProductResponseDto(
    Long roomId,
    String productName,
    String roomName,
    Integer standard,
    Integer max,
    String startDate,
    String endDate,
    String checkIn,
    String checkOut,
    String visitorName,
    String visitorPhone,
    Integer price
) {
    public ReservationProductResponseDto(ReservationProductRequestDto requestDto) {
        this(
            requestDto.roomId(),
            requestDto.productName(),
            requestDto.roomName(),
            requestDto.standard(),
            requestDto.max(),
            requestDto.startDate(),
            requestDto.endDate(),
            requestDto.checkIn(),
            requestDto.checkOut(),
            requestDto.visitorName(),
            requestDto.visitorPhone(),
            requestDto.price()
        );
    }
}
