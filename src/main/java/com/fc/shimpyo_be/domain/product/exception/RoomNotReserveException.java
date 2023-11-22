package com.fc.shimpyo_be.domain.product.exception;

import com.fc.shimpyo_be.global.exception.ApplicationException;
import com.fc.shimpyo_be.global.exception.ErrorCode;

public class RoomNotReserveException extends ApplicationException {

    protected RoomNotReserveException() {
        super(ErrorCode.ROOM_NOT_RESERVE);
    }
}
