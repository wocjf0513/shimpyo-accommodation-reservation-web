package com.fc.shimpyo_be.domain.reservation.dto.request;

import com.fc.shimpyo_be.domain.reservation.entity.PayMethod;
import com.fc.shimpyo_be.domain.reservationproduct.dto.request.ReservationProductRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

import static com.fc.shimpyo_be.domain.reservation.constant.ReservationValidationConstants.*;

@Builder
public record SaveReservationRequestDto(
    @Valid
    @Size(min = RESERVATION_REQ_MIN_SIZE, max = RESERVATION_REQ_MAX_SIZE, message = RESERVATION_REQ_SIZE_MESSAGE)
    List<ReservationProductRequestDto> reservationProducts,
    @NotNull(message = PAYMETHOD_NOTNULL_MESSAGE)
    PayMethod payMethod,
    @Min(value = TOTAL_PRICE_MIN_VALUE, message = TOTAL_PRICE_MIN_MESSAGE)
    Integer totalPrice
) {
}
