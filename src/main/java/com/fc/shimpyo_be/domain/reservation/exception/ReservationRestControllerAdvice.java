package com.fc.shimpyo_be.domain.reservation.exception;

import com.fc.shimpyo_be.domain.reservation.dto.response.ValidateReservationResultResponseDto;
import com.fc.shimpyo_be.domain.reservation.dto.response.ValidatePreoccupyResultResponseDto;
import com.fc.shimpyo_be.global.common.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ReservationRestControllerAdvice {

    @ExceptionHandler(ReserveNotAvailableException.class)
    public ResponseEntity<ResponseDto<ValidateReservationResultResponseDto>> unavailableToReserveException(
        ReserveNotAvailableException e) {
        return ResponseEntity
            .status(e.getErrorCode().getHttpStatus())
            .body(ResponseDto.res(e.getErrorCode().getHttpStatus(), e.getData(), e.getMessage()));
    }

    @ExceptionHandler(PreoccupyNotAvailableException.class)
    public ResponseEntity<ResponseDto<ValidatePreoccupyResultResponseDto>> unavailableToPreoccupyException(
        PreoccupyNotAvailableException e) {
        return ResponseEntity
            .status(e.getErrorCode().getHttpStatus())
            .body(ResponseDto.res(e.getErrorCode().getHttpStatus(), e.getData(), e.getMessage()));
    }
}
