package com.fc.shimpyo_be.domain.member.exception;

public class LoggedOutException extends RuntimeException{

    public LoggedOutException(){
        super("로그아웃 된 회원 입니다.");
    }
}
