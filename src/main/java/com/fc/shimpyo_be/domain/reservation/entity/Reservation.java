package com.fc.shimpyo_be.domain.reservation.entity;

import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.reservationproduct.entity.ReservationProduct;
import com.fc.shimpyo_be.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PayMethod payMethod;
    @Column(nullable = false)
    private int totalPrice;

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReservationProduct> reservationProducts = new ArrayList<>();

    @Builder
    public Reservation(
        Long id,
        Member member,
        PayMethod payMethod,
        int totalPrice,
        List<ReservationProduct> reservationProducts
    ) {
        this.id = id;
        this.member = member;
        this.payMethod = payMethod;
        this.totalPrice = totalPrice;

        if (!ObjectUtils.isEmpty(reservationProducts)) {
            for (ReservationProduct reservationProduct : reservationProducts) {
                this.addReservationProduct(reservationProduct);
            }
        }
    }

    public void addReservationProduct(ReservationProduct reservationProduct) {
        this.reservationProducts.add(reservationProduct);
        reservationProduct.setReservation(this);
    }

    public void minusPrice(int price) {
        this.totalPrice -= price;
    }
}
