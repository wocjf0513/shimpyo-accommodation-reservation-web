package com.fc.shimpyo_be.domain.room.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RoomResponse {

    private final Long roomCode;
    private final String roomName;
    private final Long price;
    private final String description;
    private final Long standard;
    private final Long capacity;
    private final String checkIn;
    private final String checkOut;
    private final RoomOptionResponse roomOptionResponse;
    private final List<String> roomImages;
    private Long remaining;

    @Builder
    public RoomResponse(Long roomCode, String roomName, Long price, String description, Long standard,
        Long capacity, String checkIn, String checkOut, Long remaining,
        RoomOptionResponse roomOptionResponse,
        List<String> roomImages) {
        this.roomCode = roomCode;
        this.roomName = roomName;
        this.price = price;
        this.remaining = remaining;
        this.description = description;
        this.standard = standard;
        this.capacity = capacity;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.roomOptionResponse = roomOptionResponse;
        if (roomImages == null) {
            this.roomImages = new ArrayList<>();
        } else {
            this.roomImages = roomImages;
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RoomResponse otherRoomResponse = (RoomResponse) obj;
        return this.roomCode.equals(otherRoomResponse.getRoomCode());
    }
    public void setRemaining(long remaining) {
        this.remaining = remaining;
    }

}
