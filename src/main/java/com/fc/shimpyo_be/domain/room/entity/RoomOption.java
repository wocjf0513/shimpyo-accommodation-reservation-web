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
public class RoomOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("객실 옵션 식별자")
    private Long id;
    @Column(nullable = false)
    @Comment("목욕 시설 여부")
    private boolean bathFacility;
    @Column(nullable = false)
    @Comment("욕조 여부")
    private boolean bath;
    @Column(nullable = false)
    @Comment("홈시어터 여부")
    private boolean homeTheater;
    @Column(nullable = false)
    @Comment("에어컨 여부")
    private boolean airCondition;
    @Column(nullable = false)
    @Comment("TV 여부")
    private boolean tv;
    @Column(nullable = false)
    @Comment("PC 여부")
    private boolean pc;
    @Column(nullable = false)
    @Comment("케이블 설치 여부")
    private boolean cable;
    @Column(nullable = false)
    @Comment("인터넷 여부")
    private boolean internet;
    @Column(nullable = false)
    @Comment("냉장고 여부")
    private boolean refrigerator;
    @Column(nullable = false)
    @Comment("세면도구 여부")
    private boolean toiletries;
    @Column(nullable = false)
    @Comment("소파 여부")
    private boolean sofa;
    @Column(nullable = false)
    @Comment("취사용품 여부")
    private boolean cooking;
    @Column(nullable = false)
    @Comment("테이블 여부")
    private boolean diningTable;
    @Column(nullable = false)
    @Comment("드라이기 여부")
    private boolean hairDryer;

    @Builder
    private RoomOption(Long id, boolean bathFacility, boolean bath, boolean homeTheater,
        boolean airCondition, boolean tv, boolean pc, boolean cable, boolean internet,
        boolean refrigerator, boolean toiletries, boolean sofa, boolean cooking, boolean diningTable,
        boolean hairDryer) {
        this.id = id;
        this.bathFacility = bathFacility;
        this.bath = bath;
        this.homeTheater = homeTheater;
        this.airCondition = airCondition;
        this.tv = tv;
        this.pc = pc;
        this.cable = cable;
        this.internet = internet;
        this.refrigerator = refrigerator;
        this.toiletries = toiletries;
        this.sofa = sofa;
        this.cooking = cooking;
        this.diningTable = diningTable;
        this.hairDryer = hairDryer;
    }
}
