package com.fc.shimpyo_be.domain.reservation.exception;

import com.fc.shimpyo_be.global.exception.ApplicationException;
import com.fc.shimpyo_be.global.exception.ErrorCode;

public class InvalidRequestException extends ApplicationException {

    public InvalidRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
