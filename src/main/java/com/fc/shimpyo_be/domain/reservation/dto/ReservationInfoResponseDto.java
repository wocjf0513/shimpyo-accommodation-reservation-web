package com.fc.shimpyo_be.domain.reservation.dto;

public record ReservationInfoResponseDto(
    Long reservationId,
    String productName,
    String productImageUrl,
    String productAddress,
    Long roomId,
    String roomName,
    Integer totalPeople,
    String startDate,
    String endDate,
    String checkIn,
    String checkOut,
    Integer price,
    String payMethod
) {
}
