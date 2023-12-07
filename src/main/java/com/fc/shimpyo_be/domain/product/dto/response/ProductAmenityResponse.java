package com.fc.shimpyo_be.domain.product.dto.response;

import lombok.Builder;

public record ProductAmenityResponse(

        boolean barbecue,
        boolean beauty,
        boolean beverage,
        boolean bicycle,
        boolean campfire,
        boolean fitness,
        boolean karaoke,
        boolean publicBath,
        boolean publicPc,
        boolean sauna,
        boolean sports,
        boolean seminar
) {

    @Builder
    public ProductAmenityResponse(boolean barbecue, boolean beauty, boolean beverage, boolean bicycle,
                   boolean campfire, boolean fitness, boolean karaoke, boolean publicBath, boolean publicPc,
                   boolean sauna, boolean sports, boolean seminar) {
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
