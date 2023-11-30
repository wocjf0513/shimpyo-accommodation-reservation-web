package com.fc.shimpyo_be.domain.star.exception;

import com.fc.shimpyo_be.global.exception.ApplicationException;
import com.fc.shimpyo_be.global.exception.ErrorCode;

public class CannotBeforeCheckOutException extends ApplicationException {

    public CannotBeforeCheckOutException() {
        super(ErrorCode.REGISTER_BEFORE_CHECKOUT);
    }
}
