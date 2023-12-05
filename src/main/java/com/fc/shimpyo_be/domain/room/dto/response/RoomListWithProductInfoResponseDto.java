package com.fc.shimpyo_be.domain.room.dto.response;

import java.util.List;

public record RoomListWithProductInfoResponseDto(
    List<RoomWithProductResponseDto> rooms
) {
}
