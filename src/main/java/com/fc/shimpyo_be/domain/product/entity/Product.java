package com.fc.shimpyo_be.domain.product.entity;

import com.fc.shimpyo_be.domain.room.entity.Room;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
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
    private String address;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Category category;
    @Column(nullable = false)
    private String description;
    private float starAvg;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String photoUrl;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private ArrayList<Room> rooms;

    @Builder
    public Product(Long id, String name, String address, Category category, String description, float starAvg,
        String photoUrl) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.category = category;
        this.description = description;
        this.starAvg = starAvg;
        this.photoUrl = photoUrl;
        this.rooms = new ArrayList<>();
    }
}
