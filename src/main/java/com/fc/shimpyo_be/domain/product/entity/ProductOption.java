package com.fc.shimpyo_be.domain.product.entity;

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
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("숙소 옵션 식별자")
    private Long id;
    @Column(nullable = false)
    @Comment("객실 내 취사 여부")
    private boolean cooking;
    @Column(nullable = false)
    @Comment("주차 시설 여부")
    private boolean parking;
    @Column(nullable = false)
    @Comment("픽업 서비스 여부")
    private boolean pickup;
    @Column(nullable = false)
    @Comment("식음료장")
    private String foodPlace;
    @Column(nullable = true)
    @Comment("문의 및 안내")
    private String infoCenter;

    @Builder
    public ProductOption(Long id, boolean cooking, boolean parking, boolean pickup,
        String foodPlace,
        String infoCenter) {
        this.id = id;
        this.cooking = cooking;
        this.parking = parking;
        this.pickup = pickup;
        this.foodPlace = foodPlace;
        this.infoCenter = infoCenter;
    }
}
