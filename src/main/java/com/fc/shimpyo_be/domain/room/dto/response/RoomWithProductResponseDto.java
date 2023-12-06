package com.fc.shimpyo_be.domain.room.dto.response;

public record RoomWithProductResponseDto(
    Long productId,
    String productName,
    String productThumbnail,
    String productAddress,
    Long roomId,
    String roomName,
    Integer standard,
    Integer capacity,
    String checkIn,
    String checkOut,
    Integer price
) {
}
