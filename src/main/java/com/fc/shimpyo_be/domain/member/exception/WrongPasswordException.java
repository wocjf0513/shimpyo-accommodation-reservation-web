package com.fc.shimpyo_be.domain.member.exception;

import com.fc.shimpyo_be.global.exception.ApplicationException;
import com.fc.shimpyo_be.global.exception.ErrorCode;

public class WrongPasswordException extends ApplicationException {

    public WrongPasswordException() {
        super(ErrorCode.WRONG_PASSWORD);
    }
}
