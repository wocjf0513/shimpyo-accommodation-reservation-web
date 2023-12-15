package com.fc.shimpyo_be.domain.reservation.exception;

import com.fc.shimpyo_be.domain.reservation.dto.response.ValidateReservationResultResponseDto;
import com.fc.shimpyo_be.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ReserveNotAvailableException extends RuntimeException {

    private final ErrorCode errorCode;
    private ValidateReservationResultResponseDto data;

    public ReserveNotAvailableException() {
        super(ErrorCode.RESERVATION_VALIDATION_FAIL.getSimpleMessage());
        this.errorCode = ErrorCode.RESERVATION_VALIDATION_FAIL;
    }

    public ReserveNotAvailableException(ValidateReservationResultResponseDto data) {
        super(ErrorCode.RESERVATION_VALIDATION_FAIL.getSimpleMessage());
        this.errorCode = ErrorCode.RESERVATION_VALIDATION_FAIL;
        this.data = data;
    }
}
