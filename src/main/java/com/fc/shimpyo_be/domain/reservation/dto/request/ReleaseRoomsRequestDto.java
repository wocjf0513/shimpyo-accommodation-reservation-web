package com.fc.shimpyo_be.domain.reservation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ReleaseRoomsRequestDto(
    @Valid
    @Size(min = 1, max = 3, message = "최소 1개, 최대 3개의 객실 요청 정보가 필요합니다.")
    List<ReleaseRoomItemRequestDto> rooms
) {
}
