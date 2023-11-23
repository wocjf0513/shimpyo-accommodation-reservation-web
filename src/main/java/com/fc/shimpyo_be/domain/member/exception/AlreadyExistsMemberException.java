package com.fc.shimpyo_be.domain.member.exception;

public class AlreadyExistsMemberException extends RuntimeException{

    public AlreadyExistsMemberException(){
        super("이미 가입된 회원 입니다.");
    }
}
