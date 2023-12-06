package com.fc.shimpyo_be.domain.room.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RoomPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("객실 가격 식별자")
    private Long id;
    @Column(nullable = false)
    @Comment("비수기 주중 최소 가격")
    private int offWeekDaysMinFee;
    @Column(nullable = false)
    @Comment("비수기 주말 최소 가격")
    private int offWeekendMinFee;
    @Column(nullable = false)
    @Comment("성수기 주중 최소 가격")
    private int peakWeekDaysMinFee;
    @Column(nullable = false)
    @Comment("성수기 주말 최소 가격")
    private int peakWeekendMinFee;

    @Builder
    public RoomPrice(Long id, int offWeekDaysMinFee, int offWeekendMinFee, int peakWeekDaysMinFee,
        int peakWeekendMinFee) {
        this.id = id;
        this.offWeekDaysMinFee = offWeekDaysMinFee;
        this.offWeekendMinFee = offWeekendMinFee;
        this.peakWeekDaysMinFee = peakWeekDaysMinFee;
        this.peakWeekendMinFee = peakWeekendMinFee;
    }
}
