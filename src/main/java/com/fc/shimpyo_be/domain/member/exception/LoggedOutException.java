package com.fc.shimpyo_be.domain.member.exception;

import com.fc.shimpyo_be.global.exception.ApplicationException;
import com.fc.shimpyo_be.global.exception.ErrorCode;

public class LoggedOutException extends ApplicationException {

    public LoggedOutException() {
        super(ErrorCode.LOGGED_OUT);
    }
}
