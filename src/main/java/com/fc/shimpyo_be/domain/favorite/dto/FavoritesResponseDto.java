package com.fc.shimpyo_be.domain.favorite.dto;

import com.fc.shimpyo_be.domain.product.dto.response.ProductResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FavoritesResponseDto {

    private int pageCount;
    private List<ProductResponse> products;

    @Builder
    public FavoritesResponseDto(int pageCount, List<ProductResponse> products) {
        this.pageCount = pageCount;
        this.products = products;
    }
}
