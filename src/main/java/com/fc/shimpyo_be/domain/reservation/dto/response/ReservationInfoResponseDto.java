package com.fc.shimpyo_be.domain.reservation.dto.response;

public record ReservationInfoResponseDto(
    Long reservationId,
    Long reservationProductId,
    Long productId,
    String productName,
    String productImageUrl,
    String productAddress,
    String productDetailAddress,
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
