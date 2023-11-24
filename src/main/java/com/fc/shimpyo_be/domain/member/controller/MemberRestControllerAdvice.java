package com.fc.shimpyo_be.domain.member.controller;

import com.fc.shimpyo_be.domain.member.exception.InvalidPasswordException;
import com.fc.shimpyo_be.domain.member.exception.MemberNotFoundException;
import com.fc.shimpyo_be.domain.member.exception.UnmatchedPasswordException;
import com.fc.shimpyo_be.domain.member.exception.WrongPasswordException;
import com.fc.shimpyo_be.global.common.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberRestControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> MemberNotFoundException(
        MemberNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ResponseDto.res(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> wrongPasswordException(
        WrongPasswordException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseDto.res(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> invalidPasswordException(
        InvalidPasswordException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseDto.res(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> unmatchedPasswordException(
        UnmatchedPasswordException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseDto.res(HttpStatus.BAD_REQUEST, e.getMessage()));
    }
}
