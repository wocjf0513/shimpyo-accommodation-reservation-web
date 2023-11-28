package com.fc.shimpyo_be.domain.cart.dto.response;

import lombok.Builder;

public record CartResponse(Long cartId, Long productId, String productName, String image,
                           Long roomId, String roomName, Long price, String description,
                           Long standard, Long capacity, String startDate, String endDate,
                           String checkIn, String checkOut) {

    @Builder
    public CartResponse(Long cartId, Long productId, String productName, String image, Long roomId,
        String roomName, Long price, String description, Long standard, Long capacity,
        String startDate, String endDate, String checkIn, String checkOut) {
        this.cartId = cartId;
        this.productId = productId;
        this.productName = productName;
        this.image = image;
        this.roomId = roomId;
        this.roomName = roomName;
        this.price = price;
        this.description = description;
        this.standard = standard;
        this.capacity = capacity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
}
