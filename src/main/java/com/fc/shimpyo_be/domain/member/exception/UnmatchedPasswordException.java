package com.fc.shimpyo_be.domain.member.exception;

public class UnmatchedPasswordException extends RuntimeException{

    public UnmatchedPasswordException(){
        super("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    }
}
