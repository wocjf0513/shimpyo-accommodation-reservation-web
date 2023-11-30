package com.fc.shimpyo_be.domain.product.exception;

import com.fc.shimpyo_be.global.exception.ApplicationException;
import com.fc.shimpyo_be.global.exception.ErrorCode;

public class InvalidDateException extends ApplicationException {

    public InvalidDateException() {
        super(ErrorCode.INVALID_DATE);
    }
}
