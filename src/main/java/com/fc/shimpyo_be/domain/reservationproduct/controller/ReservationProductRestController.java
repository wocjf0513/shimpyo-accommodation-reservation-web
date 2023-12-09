package com.fc.shimpyo_be.domain.reservationproduct.controller;

import com.fc.shimpyo_be.domain.reservationproduct.service.ReservationProductService;
import com.fc.shimpyo_be.global.common.ResponseDto;
import com.fc.shimpyo_be.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/reservation-products")
@RestController
public class ReservationProductRestController {

    private final ReservationProductService reservationProductService;
    private final SecurityUtil securityUtil;

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> cancel(@PathVariable Long id) {
        log.info("[api][DELETE] /api/reservation-products");

        reservationProductService.cancel(id, securityUtil.getCurrentMemberId());

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.res(HttpStatus.OK, "예약 숙소이 정상적으로 취소 처리되었습니다."));
    }
}
