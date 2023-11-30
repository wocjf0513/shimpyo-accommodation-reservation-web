package com.fc.shimpyo_be.domain.product.entity;

import java.util.Arrays;
import lombok.Builder;
import lombok.Getter;

@Getter
public enum Category {

    TOURIST_HOTEL("관광호텔", "B02010100"),
    CONDOMINIUM("콘도미니엄", "B02010500"),
    YOUTH_HOSTEL("유스호스텔", "B02010600"),
    PENSION("펜션", "B02010700"),
    MOTEL("모텔", "B02010900"),
    BED_AND_BREAKFAST("민박", "B02011000"),
    GUEST_HOUSE("게스트하우스", "B02011100"),
    HOMESTAY("홈스테이", "B02011200"),
    SERVICED_RESIDENCE("서비스드레지던스", "B02011300"),
    HANOK("한옥", "B02011600"),
    ETC("기타", "B00000000");

    private final String name;
    private final String code;

    Category(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public static Category getByName(String name) {
        return Arrays.stream(Category.values())
            .filter(val -> val.getName().equals(name))
            .findFirst()
            .orElse(Category.ETC);
    }

    public static Category getByCode(String code) {
        return Arrays.stream(Category.values())
            .filter(val -> val.getCode().equals(code))
            .findFirst()
            .orElse(Category.ETC);
    }
}
