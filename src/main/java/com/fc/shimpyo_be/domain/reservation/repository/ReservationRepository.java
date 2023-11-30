package com.fc.shimpyo_be.domain.reservation.repository;

import com.fc.shimpyo_be.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("select r.id from Reservation r where r.member.id = :memberId")
    List<Long> findIdsByMemberId(@Param("memberId") Long memberId);
}
