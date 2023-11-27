package com.fc.shimpyo_be.domain.member.exception;

import com.fc.shimpyo_be.global.exception.ApplicationException;
import com.fc.shimpyo_be.global.exception.ErrorCode;

public class AlreadyExistsMemberException extends ApplicationException {

    public AlreadyExistsMemberException() {
        super(ErrorCode.ALREADY_EXISTS_MEMBER);
    }
}
