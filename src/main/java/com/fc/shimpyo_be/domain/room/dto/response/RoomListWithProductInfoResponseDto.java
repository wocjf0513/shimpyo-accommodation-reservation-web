package com.fc.shimpyo_be.domain.room.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record RoomListWithProductInfoResponseDto(
    List<RoomWithProductResponseDto> rooms
) {
}
