package com.fc.shimpyo_be.domain.reservation.dto.response;

public record ReservationInfoResponseDto(
    Long reservationId,
    String productName,
    String productImageUrl,
    String productAddress,
    Long roomId,
    String roomName,
    String startDate,
    String endDate,
    String checkIn,
    String checkOut,
    Integer price,
    String payMethod
) {
}