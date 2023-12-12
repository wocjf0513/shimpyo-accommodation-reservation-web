package com.fc.shimpyo_be.domain.reservation.dto;

import com.fc.shimpyo_be.domain.reservation.dto.response.ValidatePreoccupyRoomResponseDto;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record CheckAvailableRoomsResultDto(
    boolean isAvailable,
    List<ValidatePreoccupyRoomResponseDto> roomResults,
    Map<Long, Map<String, String>> preoccupyMap
) {
}
