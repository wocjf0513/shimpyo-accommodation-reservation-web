package com.fc.shimpyo_be.domain.product.dto.response;

import com.fc.shimpyo_be.domain.room.dto.response.RoomResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;

public record ProductDetailsResponse(Long productId, String category,
                                     ProductAddressResponse address,
                                     String productName,
                                     String description, Boolean favorites, Float starAvg,
                                     List<String> images,
                                     ProductAmenityResponse productAmenityResponse,
                                     ProductOptionResponse productOptionResponse,
                                     List<RoomResponse> rooms) {


    @Builder
    public ProductDetailsResponse(Long productId, String category, ProductAddressResponse address,
        String productName, String description, Boolean favorites, Float starAvg,
        List<String> images, ProductAmenityResponse productAmenityResponse,
        ProductOptionResponse productOptionResponse, List<RoomResponse> rooms) {
        this.productId = productId;
        this.category = category;
        this.address = address;
        this.productName = productName;
        this.description = description;
        this.favorites = favorites;
        this.starAvg = starAvg;
        if (images == null) {
            this.images = new ArrayList<>();
        } else {
            this.images = images;
        }
        this.productAmenityResponse = productAmenityResponse;
        this.productOptionResponse = productOptionResponse;
        this.rooms = rooms;
    }
}
