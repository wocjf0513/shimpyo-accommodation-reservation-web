package com.fc.shimpyo_be.domain.room.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RoomResponse {

    private final Long roomId;
    private final String roomName;
    private final Long price;
    private final String description;
    private final Long standard;
    private final Long capacity;
    private final String checkIn;
    private final String checkOut;
    private final RoomOptionResponse roomOptionResponse;
    private final List<String> roomImages;
    private Boolean reserved;

    @Builder
    public RoomResponse(Long roomId, String roomName, Long price, String description, Long standard,
        Long capacity, String checkIn, String checkOut, Boolean reserved,
        RoomOptionResponse roomOptionResponse,
        List<String> roomImages) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.price = price;
        this.reserved = reserved;
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

    public void setReserved() {
        reserved = true;
    }

}
