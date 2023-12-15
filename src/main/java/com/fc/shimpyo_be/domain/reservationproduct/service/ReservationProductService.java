package com.fc.shimpyo_be.domain.reservationproduct.service;

import com.fc.shimpyo_be.domain.reservation.entity.Reservation;
import com.fc.shimpyo_be.domain.reservationproduct.entity.ReservationProduct;
import com.fc.shimpyo_be.domain.reservationproduct.exception.ForbiddenCancelReservationProductException;
import com.fc.shimpyo_be.domain.reservationproduct.exception.ReservationProductNotFoundException;
import com.fc.shimpyo_be.domain.reservationproduct.repository.ReservationProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationProductService {

    private final ReservationProductRepository reservationProductRepository;

    @Transactional
    public void cancel(Long id, Long memberId) {

        ReservationProduct reservationProduct = reservationProductRepository.findByIdWithReservation(id)
            .orElseThrow(ReservationProductNotFoundException::new);

        Reservation reservation = reservationProduct.getReservation();

        // 취소할 권한 확인 후, 에러 처리
        if(!reservation.getMember().getId().equals(memberId)) {
            throw new ForbiddenCancelReservationProductException();
        }

        reservationProduct.cancel();
        reservation.minusPrice(reservationProduct.getPrice());
    }
}
