package com.fc.shimpyo_be.domain.member.controller;

import com.fc.shimpyo_be.domain.member.exception.AlreadyExistsMemberException;
import com.fc.shimpyo_be.domain.member.exception.InvalidRefreshTokenException;
import com.fc.shimpyo_be.domain.member.exception.LoggedOutException;
import com.fc.shimpyo_be.domain.member.exception.MemberNotFoundException;
import com.fc.shimpyo_be.domain.member.exception.UnmatchedMemberException;
import com.fc.shimpyo_be.domain.member.exception.UnmatchedPasswordException;
import com.fc.shimpyo_be.global.common.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthRestControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> alreadySignUpException(
        AlreadyExistsMemberException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseDto.res(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> InvalidRefreshTokenException(
        InvalidRefreshTokenException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseDto.res(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> loggedOutException(
        LoggedOutException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseDto.res(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> unmatchedMemberException(
        UnmatchedMemberException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseDto.res(HttpStatus.BAD_REQUEST, e.getMessage()));
    }
}
