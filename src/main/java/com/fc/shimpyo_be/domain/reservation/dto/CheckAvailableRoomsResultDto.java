package com.fc.shimpyo_be.domain.reservation.dto;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record CheckAvailableRoomsResultDto(
    boolean isAvailable,
    List<Long> unavailableIds,
    Map<Long, Map<String, String>> recordMap
) {
}
