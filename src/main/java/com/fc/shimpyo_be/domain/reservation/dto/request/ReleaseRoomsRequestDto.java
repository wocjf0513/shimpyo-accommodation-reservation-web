package com.fc.shimpyo_be.domain.reservation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

import static com.fc.shimpyo_be.domain.reservation.constant.ReservationValidationConstants.*;

@Builder
public record ReleaseRoomsRequestDto(
    @Valid
    @Size(min = RESERVATION_REQ_MIN_SIZE, max = RESERVATION_REQ_MAX_SIZE, message = RESERVATION_REQ_SIZE_MESSAGE)
    List<ReleaseRoomItemRequestDto> rooms
) {
}
