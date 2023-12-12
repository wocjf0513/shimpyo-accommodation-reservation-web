package com.fc.shimpyo_be.domain.reservation.exception;

import com.fc.shimpyo_be.domain.reservation.dto.response.ValidatePreoccupyResultResponseDto;
import com.fc.shimpyo_be.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class PreoccupyNotAvailableException extends RuntimeException {

    private final ErrorCode errorCode;
    private ValidatePreoccupyResultResponseDto data;

    public PreoccupyNotAvailableException() {
        super(ErrorCode.UNAVAILABLE_ROOMS.getSimpleMessage());
        this.errorCode = ErrorCode.UNAVAILABLE_ROOMS;
    }

    public PreoccupyNotAvailableException(ValidatePreoccupyResultResponseDto data) {
        super(ErrorCode.UNAVAILABLE_ROOMS.getSimpleMessage());
        this.errorCode = ErrorCode.UNAVAILABLE_ROOMS;
        this.data = data;
    }
}
