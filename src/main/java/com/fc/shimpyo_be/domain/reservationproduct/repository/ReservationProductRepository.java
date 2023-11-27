package com.fc.shimpyo_be.domain.reservationproduct.repository;

import com.fc.shimpyo_be.domain.reservationproduct.entity.ReservationProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationProductRepository
    extends JpaRepository<ReservationProduct, Long>, ReservationProductRepositoryCustom {

}
