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
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("숙소 위치 식별자")
    private Long id;
    @Column(nullable = false)
    @Comment("숙소 주소")
    private String address;
    @Column(nullable = false)
    @Comment("숙소 상세 주소")
    private String detailAddress;
    @Column(nullable = false)
    @Comment("숙소 X좌표")
    private double mapX;
    @Column(nullable = false)
    @Comment("숙소 Y좌표")
    private double mapY;

    @Builder
    public Address(Long id, String address, String detailAddress, double mapX, double mapY) {
        this.id = id;
        this.address = address;
        this.detailAddress = detailAddress;
        this.mapX = mapX;
        this.mapY = mapY;
    }
}
