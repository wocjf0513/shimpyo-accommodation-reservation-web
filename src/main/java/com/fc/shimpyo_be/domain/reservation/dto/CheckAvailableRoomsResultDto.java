package com.fc.shimpyo_be.domain.reservation.dto;

import java.util.List;
import java.util.Map;

public record CheckAvailableRoomsResultDto(
    boolean isAvailable,
    List<Long> unavailableIds,
    Map<Long, Map<String, String>> recordMap
) {
}
