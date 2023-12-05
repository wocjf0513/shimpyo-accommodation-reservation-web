package com.fc.shimpyo_be.domain.cart.entity;

import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.room.entity.Room;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "장바구니 식별자")
    private Long id;
    @Comment(value = "회원 식별자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @Comment(value = "객실 식별자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
    @Comment(value = "총 이용 금액")
    @Column(nullable = false)
    private Long price;

    @Comment(value = "숙박 시작일")
    @Column(nullable = false)
    private LocalDate startDate;
    @Comment(value = "숙박 마지막일")
    @Column(nullable = false)
    private LocalDate endDate;

    @Builder
    public Cart(Room room, Member member, Long price, LocalDate startDate, LocalDate endDate) {
        this.room = room;
        this.member = member;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
