package com.fc.shimpyo_be.domain.star.exception;

import com.fc.shimpyo_be.global.exception.ApplicationException;
import com.fc.shimpyo_be.global.exception.ErrorCode;

public class ExpiredRegisterDateException extends ApplicationException {

    public ExpiredRegisterDateException() {
        super(ErrorCode.EXPIRED_STAR_REGISTER_DATE);
    }
}
