package com.fc.shimpyo_be.domain.cart.dto.response;

import lombok.Builder;

public record CartDeleteResponse(Long cartId, Long roomId, String startDate, String endDate) {

    @Builder
    public CartDeleteResponse(Long cartId, Long roomId, String startDate, String endDate) {
        this.cartId = cartId;
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
