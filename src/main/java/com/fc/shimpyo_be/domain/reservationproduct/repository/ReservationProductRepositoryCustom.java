package com.fc.shimpyo_be.domain.reservationproduct.repository;

import com.fc.shimpyo_be.domain.reservationproduct.entity.ReservationProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReservationProductRepositoryCustom {

    Page<ReservationProduct> findAllInReservationIds(List<Long> reservationIds, Pageable pageable);
}
