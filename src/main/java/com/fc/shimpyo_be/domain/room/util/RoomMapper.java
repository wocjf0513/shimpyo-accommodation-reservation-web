package com.fc.shimpyo_be.domain.room.util;

import com.fc.shimpyo_be.domain.room.dto.response.RoomResponse;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.global.util.LocalDateTimeUtil;
import java.time.LocalDate;

public class RoomMapper {

    public static RoomResponse from(Room room) {
        return RoomResponse.builder()
            .roomId(room.getId())
            .roomName(room.getName())
            .price((long)(room.getPrice()))
            .standard((long)(room.getStandard()))
            .capacity((long) room.getCapacity())
            .desc(room.getDescription())
            .checkIn(LocalDateTimeUtil.toString(room.getCheckIn()))
            .checkOut(LocalDateTimeUtil.toString(room.getCheckOut()))
            .build();
    }
}
