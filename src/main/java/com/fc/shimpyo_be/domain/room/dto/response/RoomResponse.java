package com.fc.shimpyo_be.domain.room.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
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
    private Boolean reserved;

    public void setReserved() {
        reserved=true;
    }

}
