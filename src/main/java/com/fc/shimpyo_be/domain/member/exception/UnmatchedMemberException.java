package com.fc.shimpyo_be.domain.member.exception;

public class UnmatchedMemberException extends RuntimeException{

    public UnmatchedMemberException(){
        super("토큰의 회원 정보가 일치하지 않습니다.");
    }
}
