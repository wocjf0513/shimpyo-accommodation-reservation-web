package com.fc.shimpyo_be.domain.cart.dto.response;

import lombok.Builder;
import lombok.Getter;


@Getter
public class CartResponse {

    private final Long cartId;
    private final Long productId;
    private final String productName;
    private final String image;
    private final Long roomId;
    private final String roomName;
    private final Long price;
    private final String description;
    private final Long standard;
    private final Long capacity;
    private final String startDate;
    private final String endDate;
    private final String checkIn;
    private final String checkOut;
    private Boolean reserved = false;

    public void setReserved(){
        reserved = true;
    }

    @Builder
    public CartResponse(Long cartId, Long productId, String productName, String image, Long roomId,
        String roomName, Long price, String description, Long standard, Long capacity,
        String startDate, String endDate, String checkIn, String checkOut, Boolean reserved) {
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
