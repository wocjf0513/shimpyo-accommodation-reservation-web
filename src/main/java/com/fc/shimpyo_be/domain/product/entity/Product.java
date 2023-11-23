package com.fc.shimpyo_be.domain.product.entity;

import com.fc.shimpyo_be.domain.room.entity.Room;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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
    @Column(nullable = false, length = 30)
    private String category;
    @Column(nullable = false)
    private String description;
    @ColumnDefault("0")
    private float starAvg;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String photoUrl;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms = new ArrayList<>();

    @Builder
    public Product(Long id, String name, String address, Category category, String description, float starAvg,
        String photoUrl) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.category = category.toString();
        this.description = description;
        this.starAvg = starAvg;
        this.photoUrl = photoUrl;
    }
}
