package com.fc.shimpyo_be.domain.product.dto.response;

import lombok.Builder;


public record ProductResponse(Long productId, String category, String address, String productName,
                              Float starAvg, String image, Long price, Boolean favorites,
                              Long capacity) {

    @Builder
    public ProductResponse(Long productId, String category, String address, String productName,
        Float starAvg, String image, Long price, Boolean favorites, Long capacity) {
        this.productId = productId;
        this.category = category;
        this.address = address;
        this.productName = productName;
        this.starAvg = starAvg;
        this.image = image;
        this.price = price;
        this.favorites = favorites;
        this.capacity = capacity;
    }
}
