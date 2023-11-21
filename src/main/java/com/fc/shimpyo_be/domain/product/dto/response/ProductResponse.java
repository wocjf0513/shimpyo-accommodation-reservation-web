package com.fc.shimpyo_be.domain.product.dto.response;

import com.fc.shimpyo_be.domain.product.entity.Category;
import lombok.Builder;

@Builder
public record ProductResponse(Long productId, String category, String address, String productName, Float star,
                              String image) {

}
