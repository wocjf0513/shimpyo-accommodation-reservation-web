package com.fc.shimpyo_be.domain.reservation.controller;

import com.fc.shimpyo_be.domain.reservation.dto.request.PreoccupyRoomsRequestDto;
import com.fc.shimpyo_be.domain.reservation.facade.PreoccupyRoomsLockFacade;
import com.fc.shimpyo_be.global.common.ResponseDto;
import com.fc.shimpyo_be.global.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
@RestController
public class ReservationRestController {

    private final PreoccupyRoomsLockFacade preoccupyRoomsLockFacade;
    private final SecurityUtil securityUtil;

    @PostMapping("/preoccupy")
    public ResponseEntity<ResponseDto<Void>> checkAvailableAndPreoccupy(@Valid @RequestBody PreoccupyRoomsRequestDto request) {
        log.info("[api][POST] /api/reservations/preoccupy");

        preoccupyRoomsLockFacade.checkAvailableAndPreoccupy(securityUtil.getCurrentMemberId(), request);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                ResponseDto.res(HttpStatus.OK, "예약 가능 유효성 검사와 객실 선점이 정상적으로 완료되었습니다.")
            );
    }
}
