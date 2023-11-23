package com.fc.shimpyo_be.global.exception;

import com.fc.shimpyo_be.global.common.ResponseDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> bindException(BindException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ResponseDto.res(HttpStatus.BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> usernameNotFoundException(
        UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ResponseDto.res(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> ApplicationException(ApplicationException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(
            ResponseDto.res(e.getErrorCode().getHttpStatus(),
                e.getErrorCode().getSimpleMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> ConstraintViolationException(
        ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ResponseDto.res(HttpStatus.BAD_REQUEST,
                e.getMessage()));
    }

}
