package com.fc.shimpyo_be.domain.room.util;

import com.fc.shimpyo_be.domain.room.dto.response.RoomResponse;
import com.fc.shimpyo_be.domain.room.entity.Room;

public class RoomMapper {

    public static RoomResponse from(Room room) {
        return RoomResponse.builder()
            .roomId(room.getId())
            .roomName(room.getName())
            // TODO 날짜에 따라 가격이 달라지므로 로직 수정이 필요함
            .price((long) (room.getPrice().getPeakWeekendMinFee()))
            .standard((long) (room.getStandard()))
            .capacity((long) room.getCapacity())
            .description(room.getDescription())
            .reserved(false)
            .checkIn(room.getCheckIn().toString())
            .checkOut(room.getCheckOut().toString())
            .build();
    }
}
