package com.fc.shimpyo_be.domain.reservation.repository;

import com.fc.shimpyo_be.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
