package com.fc.shimpyo_be.domain.product.exception;

import com.fc.shimpyo_be.global.exception.ApplicationException;
import com.fc.shimpyo_be.global.exception.ErrorCode;

public class RoomNotFoundException extends ApplicationException {

    public RoomNotFoundException() {
        super(ErrorCode.ROON_NOT_FOUND);
    }
}
