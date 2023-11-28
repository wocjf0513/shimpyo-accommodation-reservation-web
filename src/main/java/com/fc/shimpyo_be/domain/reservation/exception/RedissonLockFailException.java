package com.fc.shimpyo_be.domain.reservation.exception;

import com.fc.shimpyo_be.global.exception.ApplicationException;
import com.fc.shimpyo_be.global.exception.ErrorCode;

public class RedissonLockFailException extends ApplicationException {

    public RedissonLockFailException() {
        super(ErrorCode.LOCK_FAIL);
    }
}
