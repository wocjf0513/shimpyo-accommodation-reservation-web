package com.fc.shimpyo_be.domain.reservationproduct.repository;

import com.fc.shimpyo_be.domain.reservationproduct.entity.ReservationProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReservationProductRepository
    extends JpaRepository<ReservationProduct, Long>, ReservationProductRepositoryCustom {

    @Query("select rp from ReservationProduct rp join fetch rp.reservation where rp.id = :id")
    Optional<ReservationProduct> findByIdWithReservation(@Param("id") Long id);

    @Query("select rp from ReservationProduct rp join fetch rp.room where rp.id = :id")
    Optional<ReservationProduct> findByIdWithRoom(@Param("id") Long id);
}
