package com.fc.shimpyo_be.domain.cart.dto.response;

import lombok.Builder;

public record CartDeleteResponse(Long cartId, Long roomCode, String startDate, String endDate) {

    @Builder
    public CartDeleteResponse(Long cartId, Long roomCode, String startDate, String endDate) {
        this.cartId = cartId;
        this.roomCode = roomCode;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
