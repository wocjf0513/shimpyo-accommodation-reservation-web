package com.fc.shimpyo_be.domain.member.exception;

public class WrongPasswordException extends RuntimeException{

    public WrongPasswordException(){
        super("비밀번호가 틀렸습니다.");
    }
}
