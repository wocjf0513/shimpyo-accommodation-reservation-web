package com.fc.shimpyo_be.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckPasswordResponseDto {

    private boolean isCorrectPassword;

    @Builder
    public CheckPasswordResponseDto(boolean isCorrectPassword) {
        this.isCorrectPassword = isCorrectPassword;
    }
}
