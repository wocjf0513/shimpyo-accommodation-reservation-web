package com.fc.shimpyo_be.domain.room.entity;

import com.fc.shimpyo_be.domain.product.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(length = 30)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(columnDefinition = "TINYINT")
    private int standard;
    @Column(columnDefinition = "TINYINT")
    private int max;
    @Column(nullable = false)
    private int price;
    @Column(nullable = false)
    private LocalDateTime checkIn;
    @Column(nullable = false)
    private LocalDateTime checkOut;

    @Builder
    public Room(Long id, Product product, String name, String description, int standard, int max,
        int price, LocalDateTime checkIn, LocalDateTime checkOut) {
        this.id = id;
        this.product = product;
        this.name = name;
        this.description = description;
        this.standard = standard;
        this.max = max;
        this.price = price;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
}
