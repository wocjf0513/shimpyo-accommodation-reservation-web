package com.fc.shimpyo_be.domain.product.dto.response;

import lombok.Builder;

public record ProductAddressResponse (
    String address,
    String detailAddress,
    double mapX,
    double mapY
) {

    @Builder
    public ProductAddressResponse(String address, String detailAddress, double mapX, double mapY) {
        this.address = address;
        this.detailAddress = detailAddress;
        this.mapX = mapX;
        this.mapY = mapY;
    }
}
