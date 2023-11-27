package com.fc.shimpyo_be.domain.reservation.entity;

import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder
    public Reservation(
        Long id,
        Member member,
        PayMethod payMethod,
        int totalPrice
    ) {
        this.id = id;
        this.member = member;
        this.payMethod = payMethod;
        this.totalPrice = totalPrice;
    }
}
