package com.fc.shimpyo_be.domain.cart.exception;

import com.fc.shimpyo_be.global.exception.ApplicationException;
import com.fc.shimpyo_be.global.exception.ErrorCode;

public class CartNotFoundException extends ApplicationException {

    public CartNotFoundException() {
        super(ErrorCode.CART_NOT_FOUND);
    }
}
