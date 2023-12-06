package com.fc.shimpyo_be.domain.reservationproduct.dto.response;

public record ReservationProductResponseDto(
    String productName,
    Long roomId,
    String roomName,
    Integer standard,
    Integer capacity,
    String startDate,
    String endDate,
    String checkIn,
    String checkOut,
    String visitorName,
    String visitorPhone,
    Integer price
) {
}
