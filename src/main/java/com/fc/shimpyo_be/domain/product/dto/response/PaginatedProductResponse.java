package com.fc.shimpyo_be.domain.product.dto.response;


import java.util.ArrayList;
import java.util.List;
import lombok.Builder;

public record PaginatedProductResponse(

    List<ProductResponse> productResponses,
    int pageCount
) {

    @Builder
    public PaginatedProductResponse(List<ProductResponse> productResponses, int pageCount) {
        if (productResponses != null) {
            this.productResponses = productResponses;
        } else {
            this.productResponses = new ArrayList<>();
        }
        this.pageCount = pageCount;
    }
}