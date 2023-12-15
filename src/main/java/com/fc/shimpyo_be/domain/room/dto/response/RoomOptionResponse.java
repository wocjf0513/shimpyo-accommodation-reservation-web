package com.fc.shimpyo_be.domain.room.dto.response;

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

public record RoomOptionResponse(

    boolean bathFacility,
    boolean bath,
    boolean homeTheater,
    boolean airCondition,
    boolean tv,
    boolean pc,
    boolean cable,
    boolean internet,
    boolean refrigerator,
    boolean toiletries,
    boolean sofa,
    boolean cooking,
    boolean table,
    boolean hairDryer

) {

    @Builder
    public RoomOptionResponse(boolean bathFacility, boolean bath, boolean homeTheater,
        boolean airCondition, boolean tv, boolean pc, boolean cable, boolean internet,
        boolean refrigerator, boolean toiletries, boolean sofa, boolean cooking, boolean table,
        boolean hairDryer) {
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
        this.table = table;
        this.hairDryer = hairDryer;
    }
}
