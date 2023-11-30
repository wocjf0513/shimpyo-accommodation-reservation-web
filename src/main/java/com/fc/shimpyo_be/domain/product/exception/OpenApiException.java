package com.fc.shimpyo_be.domain.product.exception;

import com.fc.shimpyo_be.global.exception.ApplicationException;
import com.fc.shimpyo_be.global.exception.ErrorCode;

public class OpenApiException extends ApplicationException {

    public OpenApiException() {
        super(ErrorCode.OPEN_API_ERROR);
    }
}
