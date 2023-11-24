package com.fc.shimpyo_be.domain.room.util;

import com.fc.shimpyo_be.domain.room.dto.response.RoomResponse;
import com.fc.shimpyo_be.domain.room.entity.Room;

public class RoomMapper {

    public static RoomResponse from(Room room) {
        return RoomResponse.builder()
            .roomId(room.getId())
            .roomName(room.getName())
            .price((long) (room.getPrice()))
            .standard((long) (room.getStandard()))
            .capacity((long) room.getCapacity())
            .description(room.getDescription())
            .reserved(false)
            .checkIn(room.getCheckIn().toString())
            .checkOut(room.getCheckOut().toString())
            .build();
    }
}
