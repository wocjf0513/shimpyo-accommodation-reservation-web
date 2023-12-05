package com.fc.shimpyo_be.domain.reservationproduct.entity;

import com.fc.shimpyo_be.domain.reservation.entity.Reservation;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReservationProduct extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "예약 상품 식별자")
    private Long id;
    @Comment(value = "예약 주문 식별자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
    @Comment(value = "예약 객실 식별자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;
    @Comment(value = "총 이용 금액")
    @Column(nullable = false)
    private int price;
    @Comment(value = "숙박 시작일")
    @Column(nullable = false)
    private LocalDate startDate;
    @Comment(value = "숙박 마지막일")
    @Column(nullable = false)
    private LocalDate endDate;
    @Comment(value = "방문자명")
    private String visitorName;
    @Comment(value = "방문자 전화번호")
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
