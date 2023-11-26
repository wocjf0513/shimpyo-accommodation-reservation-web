package com.fc.shimpyo_be.domain.member.exception;

import com.fc.shimpyo_be.global.exception.ApplicationException;
import com.fc.shimpyo_be.global.exception.ErrorCode;

public class UnmatchedMemberException extends ApplicationException {

    public UnmatchedMemberException() {
        super(ErrorCode.UNMATCHED_MEMBER);
    }
}
