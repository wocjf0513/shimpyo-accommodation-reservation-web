package com.fc.shimpyo_be.domain.star.controller;

import com.fc.shimpyo_be.domain.star.exception.EntityNotFoundException;
import com.fc.shimpyo_be.global.common.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class StarRestControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> entityNotFoundException(EntityNotFoundException e) {
        log.info("handling {}, message: {}", e.getClass().getSimpleName(), e.getMessage());

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ResponseDto.res(HttpStatus.BAD_REQUEST, e.getMessage()));
    }
}
