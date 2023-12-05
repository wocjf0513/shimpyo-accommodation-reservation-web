package com.fc.shimpyo_be.domain.room.repository;

import com.fc.shimpyo_be.domain.room.dto.response.RoomWithProductResponseDto;

import java.util.List;

public interface RoomRepositoryCustom {

    List<RoomWithProductResponseDto> findAllInRoomIdsResponseDto(List<Long> roomIds);
}
