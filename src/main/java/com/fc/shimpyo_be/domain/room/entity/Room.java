package com.fc.shimpyo_be.domain.room.entity;

import com.fc.shimpyo_be.domain.product.entity.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("객실 식별자")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @Comment("숙소 식별자")
    private Product product;
    @Column(length = 30)
    @Comment("객실 이름")
    private String name;
    @Column(nullable = false)
    @Comment("객실 설명")
    private String description;
    @Column(columnDefinition = "TINYINT")
    @Comment("객실 기준인원")
    private int standard;
    @Column(columnDefinition = "TINYINT")
    @Comment("객실 최대인원")
    private int capacity;
    @Column(columnDefinition = "TIME")
    @Comment("객실 체크인 시간")
    private LocalTime checkIn;
    @Column(columnDefinition = "TIME")
    @Comment("객실 체크아웃 시간")
    private LocalTime checkOut;
    @Column(nullable = false)
    @Comment("객실 가격")
    private int price;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("객실 옵션 식별자")
    private RoomOption roomOption;
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomImage> roomImages = new ArrayList<>();


    @Builder
    public Room(
        Long id,
        Product product,
        String name,
        String description,
        int standard,
        int capacity,
        LocalTime checkIn,
        LocalTime checkOut,
        int price,
        RoomOption roomOption,
        List<RoomImage> roomImages
    ) {
        this.id = id;
        this.product = product;
        this.name = name;
        this.description = description;
        this.standard = standard;
        this.capacity = capacity;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.price = price;
        this.roomOption = roomOption;
        this.roomImages = roomImages;
    }
}
