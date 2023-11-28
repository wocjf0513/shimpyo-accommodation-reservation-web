package com.fc.shimpyo_be.domain.reservation.dto.response;

import java.util.List;

public record ValidationResultResponseDto(
    boolean isAvailable,
    List<Long> unavailableIds
) {
}
