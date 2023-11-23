package com.fc.shimpyo_be.domain.product.entity;

import java.util.Arrays;

public enum Category {

    HOTEL("호텔"),
    MOTEL("모텔"),
    POOL_VILA("풀빌라"),
    RENTAL_COTTAGE("펜션");
    private final String name;

    public String getName(){
        return name;
    }

    Category(String name) {
        this.name = name;
    }

    public static Category getByName(String name) {
        return Arrays.stream(Category.values())
            .filter(val -> val.getName().equals(name))
            .findFirst()
            .orElse(Category.HOTEL);
    }



}
