package com.fc.shimpyo_be.domain.product.entity;

import com.fc.shimpyo_be.domain.product.util.CategoryConverter;
import com.fc.shimpyo_be.domain.room.entity.Room;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("숙소 식별자")
    private Long id;
    @Column(nullable = false)
    @Comment("숙소 이름")
    private String name;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("숙소 위치")
    private Address address;
    @Column(nullable = false)
    @Convert(converter = CategoryConverter.class)
    @Comment("숙소 카테고리")
    private Category category;
    @Column(columnDefinition = "TEXT", nullable = false)
    @Comment("숙소 설명")
    private String description;
    @ColumnDefault("0")
    @Comment("숙소 평점")
    private float starAvg;
    @Column(columnDefinition = "TEXT", nullable = false)
    @Comment("숙소 대표 이미지 URL")
    private String thumbnail;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> photoUrls = new ArrayList<>();
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms = new ArrayList<>();
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductOption productOption;

    @Builder
    public Product(Long id, String name, Address address, Category category, String description,
        float starAvg, String thumbnail, List<ProductImage> photoUrls, List<Room> rooms,
        ProductOption productOption) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.category = category;
        this.description = description;
        this.starAvg = starAvg;
        this.thumbnail = thumbnail;
        this.photoUrls = photoUrls;
        this.rooms = rooms;
        this.productOption = productOption;
    }

    public void updateStarAvg(float starAvg) {
        this.starAvg = starAvg;
    }
}
