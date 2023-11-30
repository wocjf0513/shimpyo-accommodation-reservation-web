package com.fc.shimpyo_be.domain.member.exception;

import com.fc.shimpyo_be.global.exception.ApplicationException;
import com.fc.shimpyo_be.global.exception.ErrorCode;

public class InvalidRefreshTokenException extends ApplicationException {

    public InvalidRefreshTokenException() {
        super(ErrorCode.INVALID_REFRESH_TOKEN);
    }
}
