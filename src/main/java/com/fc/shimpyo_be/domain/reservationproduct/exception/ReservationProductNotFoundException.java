package com.fc.shimpyo_be.domain.reservationproduct.exception;

import com.fc.shimpyo_be.global.exception.ApplicationException;
import com.fc.shimpyo_be.global.exception.ErrorCode;

public class ReservationProductNotFoundException extends ApplicationException {

    public ReservationProductNotFoundException() {
        super(ErrorCode.RESERVATION_PRODUCT_NOT_FOUND);
    }
}
