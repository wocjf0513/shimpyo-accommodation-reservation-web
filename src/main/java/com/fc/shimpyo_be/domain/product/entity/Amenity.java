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
public class Amenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("숙소 부대시설 식별자")
    private Long id;
    @Column(nullable = false)
    @Comment("바비큐장 여부")
    private boolean barbecue;
    @Column(nullable = false)
    @Comment("뷰티시설 여부")
    private boolean beauty;
    @Column(nullable = false)
    @Comment("식음료장 여부")
    private boolean beverage;
    @Column(nullable = false)
    @Comment("자전거 대여 여부")
    private boolean bicycle;
    @Column(nullable = false)
    @Comment("캠프파이어 여부")
    private boolean campfire;
    @Column(nullable = false)
    @Comment("휘트니스 센터 여부")
    private boolean fitness;
    @Column(nullable = false)
    @Comment("노래방 여부")
    private boolean karaoke;
    @Column(nullable = false)
    @Comment("공동 샤워실 여부")
    private boolean publicBath;
    @Column(nullable = false)
    @Comment("공동 PC실 여부")
    private boolean publicPc;
    @Column(nullable = false)
    @Comment("사우나 여부")
    private boolean sauna;
    @Column(nullable = false)
    @Comment("스포츠 시설 여부")
    private boolean sports;
    @Column(nullable = false)
    @Comment("세미나실 여부")
    private boolean seminar;

    @Builder
    public Amenity(Long id, boolean barbecue, boolean beauty, boolean beverage, boolean bicycle,
        boolean campfire, boolean fitness, boolean karaoke, boolean publicBath, boolean publicPc,
        boolean sauna, boolean sports, boolean seminar) {
        this.id = id;
        this.barbecue = barbecue;
        this.beauty = beauty;
        this.beverage = beverage;
        this.bicycle = bicycle;
        this.campfire = campfire;
        this.fitness = fitness;
        this.karaoke = karaoke;
        this.publicBath = publicBath;
        this.publicPc = publicPc;
        this.sauna = sauna;
        this.sports = sports;
        this.seminar = seminar;
    }
}
