package com.fc.shimpyo_be.domain.member.exception;

import com.fc.shimpyo_be.global.exception.ApplicationException;
import com.fc.shimpyo_be.global.exception.ErrorCode;

public class MemberNotFoundException extends ApplicationException {

    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
