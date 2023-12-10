package com.fc.shimpyo_be.domain.product.dto.response;

import com.fc.shimpyo_be.domain.product.entity.Amenity;
import com.fc.shimpyo_be.domain.product.entity.ProductImage;
import com.fc.shimpyo_be.domain.room.dto.response.RoomResponse;

import java.util.List;

import lombok.Builder;

public record ProductDetailsResponse(Long productId, String category, String address,
                                     String productName,
                                     String description, Boolean favorites, Float starAvg,
                                     List<String> images,
                                     ProductAmenityResponse productAmenityResponse,
                                     ProductOptionResponse productOptionResponse,
                                     List<RoomResponse> rooms) {


    @Builder
    public ProductDetailsResponse(Long productId, String category, String address,
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
        this.images = images;
        this.productAmenityResponse = productAmenityResponse;
        this.productOptionResponse = productOptionResponse;
        this.rooms = rooms;
    }
}
