package com.fc.shimpyo_be.domain.member.exception;

public class InvalidRefreshTokenException extends RuntimeException{

    public InvalidRefreshTokenException(){
        super("Refresh Token 이 유효하지 않습니다.");
    }
}
