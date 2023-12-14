package com.fc.shimpyo_be.domain.favorite.dto;

import com.fc.shimpyo_be.domain.product.dto.response.ProductResponse;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoritesResponseDto {

    private int pageCount;
    private List<ProductResponse> products;

    @Builder
    private FavoritesResponseDto(int pageCount, List<ProductResponse> products) {
        this.pageCount = pageCount;
        this.products = products;
    }
}
