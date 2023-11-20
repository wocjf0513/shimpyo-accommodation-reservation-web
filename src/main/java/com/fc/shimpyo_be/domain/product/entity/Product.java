package com.fc.shimpyo_be.domain.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 30)
    private String name;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Category category;
    @Column(nullable = false)
    private String description;
    private float starAvg;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String photoUrl;

    @Builder
    public Product(Long id, String name, Category category, String description, float starAvg,
        String photoUrl) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.starAvg = starAvg;
        this.photoUrl = photoUrl;
    }
}
