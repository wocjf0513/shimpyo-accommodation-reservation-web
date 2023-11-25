package com.fc.shimpyo_be.domain.member.exception;

public class InvalidPasswordException extends RuntimeException{

    public InvalidPasswordException() {
        super("비밀번호 양식에 맞지 않습니다.");
    }
}
