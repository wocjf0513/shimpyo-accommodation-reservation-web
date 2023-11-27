package com.fc.shimpyo_be.domain.reservation.service;

import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.reservation.dto.ReservationInfoResponseDto;
import com.fc.shimpyo_be.domain.reservation.entity.Reservation;
import com.fc.shimpyo_be.domain.reservation.repository.ReservationRepository;
import com.fc.shimpyo_be.domain.reservationproduct.repository.ReservationProductRepository;
import com.fc.shimpyo_be.domain.room.entity.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationProductRepository reservationProductRepository;

    @Transactional(readOnly = true)
    public Page<ReservationInfoResponseDto> getReservationInfoList(Long memberId, Pageable pageable) {
        log.info("{} ::: {}", getClass().getSimpleName(), "getReservationInfoList");

        List<Long> reservationIds = reservationRepository.findIdsByMemberId(memberId);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        return reservationProductRepository
            .findAllInReservationIds(reservationIds, pageable)
            .map(
                reservationProduct -> {
                    Reservation reservation = reservationProduct.getReservation();
                    Room room = reservationProduct.getRoom();
                    Product product = room.getProduct();

                    return new ReservationInfoResponseDto(
                        reservation.getId(),
                        product.getName(),
                        product.getPhotoUrl(),
                        product.getAddress(),
                        room.getId(),
                        room.getName(),
                        reservationProduct.getTotalPeople(),
                        dateFormatter.format(reservationProduct.getStartDate()),
                        dateFormatter.format(reservationProduct.getEndDate()),
                        timeFormatter.format(room.getCheckIn()),
                        timeFormatter.format(room.getCheckOut()),
                        reservationProduct.getPrice(),
                        reservation.getPayMethod().name()
                    );
                }
            );
    }
}
