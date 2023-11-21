package com.fc.shimpyo_be.domain.product.exception;

import com.fc.shimpyo_be.global.exception.ApplicationException;
import com.fc.shimpyo_be.global.exception.ErrorCode;
import org.springframework.http.HttpStatusCode;

public class ProductNotFoundException extends ApplicationException {

    private static final ErrorCode errorCode = ErrorCode.PRODUCT_NOT_FOUND;
    ProductNotFoundException () {
        super(errorCode);
    }
}
