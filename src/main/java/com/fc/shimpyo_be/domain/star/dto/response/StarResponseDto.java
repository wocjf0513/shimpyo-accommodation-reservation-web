package com.fc.shimpyo_be.domain.star.dto.response;

import com.fc.shimpyo_be.domain.star.entity.Star;

public record StarResponseDto(
    Long starId,
    float score
) {
    public StarResponseDto(Star entity) {
        this(entity.getId(), entity.getScore());
    }
}
