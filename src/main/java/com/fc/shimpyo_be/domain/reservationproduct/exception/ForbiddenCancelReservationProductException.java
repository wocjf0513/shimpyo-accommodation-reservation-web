package com.fc.shimpyo_be.domain.reservationproduct.exception;

import com.fc.shimpyo_be.global.exception.ApplicationException;
import com.fc.shimpyo_be.global.exception.ErrorCode;

public class ForbiddenCancelReservationProductException extends ApplicationException {
    public ForbiddenCancelReservationProductException() {
        super(ErrorCode.FORBIDDEN_CANCEL_RESERVATION_PRODUCT);
    }
}
