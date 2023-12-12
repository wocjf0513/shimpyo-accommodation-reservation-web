package com.fc.shimpyo_be.domain.reservation.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ValidatePreoccupyResultResponseDto(
    boolean isAvailable,
    List<ValidatePreoccupyRoomResponseDto> roomResults
) {
}
