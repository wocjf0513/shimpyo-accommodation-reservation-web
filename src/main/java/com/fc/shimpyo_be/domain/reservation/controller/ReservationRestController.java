package com.fc.shimpyo_be.domain.reservation.controller;

import com.fc.shimpyo_be.domain.reservation.service.ReservationService;
import com.fc.shimpyo_be.global.common.ResponseDto;
import com.fc.shimpyo_be.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
@RestController
public class ReservationRestController {

    private final ReservationService reservationService;
    private final SecurityUtil securityUtil;

    @GetMapping
    public ResponseEntity<?> getReservationList(
        @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        log.info("[api][GET] /api/reservations");

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                ResponseDto.res(
                    HttpStatus.OK,
                    reservationService.getReservationInfoList(securityUtil.getCurrentMemberId(), pageable),
                    "전체 주문 목록 조회 성공"
                )
            );
    }
}
