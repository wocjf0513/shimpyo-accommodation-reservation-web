package com.fc.shimpyo_be.domain.reservation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PreoccupyRoomsRequestDto(

    @Valid
    @Size(min = 1, max = 3, message = "최소 1개, 최대 3개의 객실 예약이 가능합니다.")
    List<PreoccupyRoomItemRequestDto> rooms
) {
}
