package com.fc.shimpyo_be.domain.product.dto.response;

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

public record ProductOptionResponse(
        boolean cooking,
        boolean parking,
        boolean pickup,
        String foodPlace,
        String infoCenter

) {
    @Builder
    public ProductOptionResponse(boolean cooking, boolean parking, boolean pickup,
                                 String foodPlace,
                                 String infoCenter) {
        this.cooking = cooking;
        this.parking = parking;
        this.pickup = pickup;
        this.foodPlace = foodPlace;
        this.infoCenter = infoCenter;
    }
}
