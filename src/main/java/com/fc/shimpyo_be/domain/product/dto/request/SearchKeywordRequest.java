package com.fc.shimpyo_be.domain.product.dto.request;

import lombok.Builder;

public record SearchKeywordRequest(String productName, String address, String category) {

    @Builder
    public SearchKeywordRequest(String productName, String address, String category) {
        this.productName = productName;
        this.address = address;
        this.category = category;
    }
}
