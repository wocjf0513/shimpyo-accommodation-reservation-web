package com.fc.shimpyo_be.domain.reservation.exception;

import com.fc.shimpyo_be.domain.reservation.dto.response.PreoccupyRoomsResponseDto;
import com.fc.shimpyo_be.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class UnavailableRoomsException extends RuntimeException {

    private final ErrorCode errorCode;
    private PreoccupyRoomsResponseDto data;

    public UnavailableRoomsException() {
        super(ErrorCode.UNAVAILABLE_ROOMS.getSimpleMessage());
        this.errorCode = ErrorCode.UNAVAILABLE_ROOMS;
    }

    public UnavailableRoomsException(PreoccupyRoomsResponseDto data) {
        super(ErrorCode.UNAVAILABLE_ROOMS.getSimpleMessage());
        this.errorCode = ErrorCode.UNAVAILABLE_ROOMS;
        this.data = data;
    }
}
