package com.fc.shimpyo_be.domain.reservationproduct.entity;

import com.fc.shimpyo_be.domain.reservation.entity.Reservation;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReservationProduct extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;
    @Column(nullable = false)
    private int price;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    private String visitorName;
    private String visitorPhone;

    @Builder
    public ReservationProduct(
        Long id,
        Reservation reservation,
        Room room,
        int price,
        LocalDate startDate,
        LocalDate endDate,
        String visitorName,
        String visitorPhone
    ) {
        this.id = id;
        this.reservation = reservation;
        this.room = room;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.visitorName = visitorName;
        this.visitorPhone = visitorPhone;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public void cancel() {
        this.delete(LocalDateTime.now());
    }
}
