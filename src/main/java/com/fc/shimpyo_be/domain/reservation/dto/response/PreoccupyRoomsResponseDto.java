package com.fc.shimpyo_be.domain.reservation.dto.response;

import java.util.List;

public record PreoccupyRoomsResponseDto(
    boolean isAvailable,
    List<Long> unavailableIds
) {
}
