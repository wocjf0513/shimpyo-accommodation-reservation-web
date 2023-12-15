package com.fc.shimpyo_be.domain.favorite.dto;

import com.fc.shimpyo_be.domain.favorite.entity.Favorite;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteResponseDto {

    private Long favoriteId;
    private Long memberId;
    private Long productId;

    @Builder
    private FavoriteResponseDto(Long favoriteId, Long memberId, Long productId) {
        this.favoriteId = favoriteId;
        this.memberId = memberId;
        this.productId = productId;
    }

    public static FavoriteResponseDto of(Favorite favorite) {
        return FavoriteResponseDto.builder()
            .favoriteId(favorite.getId())
            .memberId(favorite.getMember().getId())
            .productId(favorite.getProduct().getId())
            .build();
    }
}
