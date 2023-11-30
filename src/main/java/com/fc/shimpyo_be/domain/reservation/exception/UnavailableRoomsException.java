package com.fc.shimpyo_be.domain.reservation.exception;

import com.fc.shimpyo_be.domain.reservation.dto.response.ValidationResultResponseDto;
import com.fc.shimpyo_be.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class UnavailableRoomsException extends RuntimeException {

    private final ErrorCode errorCode;
    private ValidationResultResponseDto data;

    public UnavailableRoomsException() {
        super(ErrorCode.UNAVAILABLE_ROOMS.getSimpleMessage());
        this.errorCode = ErrorCode.UNAVAILABLE_ROOMS;
    }

    public UnavailableRoomsException(ValidationResultResponseDto data) {
        super(ErrorCode.UNAVAILABLE_ROOMS.getSimpleMessage());
        this.errorCode = ErrorCode.UNAVAILABLE_ROOMS;
        this.data = data;
    }
}
