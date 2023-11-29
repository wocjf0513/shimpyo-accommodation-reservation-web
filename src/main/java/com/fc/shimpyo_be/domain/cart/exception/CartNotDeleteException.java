package com.fc.shimpyo_be.domain.cart.exception;

import com.fc.shimpyo_be.global.exception.ApplicationException;
import com.fc.shimpyo_be.global.exception.ErrorCode;

public class CartNotDeleteException extends ApplicationException {

    public CartNotDeleteException() {
        super(ErrorCode.CART_NOT_DELETE);
    }
}
