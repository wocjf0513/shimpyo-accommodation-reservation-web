package com.fc.shimpyo_be.domain.room.dto.response;

import lombok.Builder;

@Builder
public record RoomResponse(Long roomId, String roomName, Long price, String desc, Long standard,
                           Long capacity, String checkIn, String checkOut) {

}
