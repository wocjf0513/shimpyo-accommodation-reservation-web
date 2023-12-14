package com.fc.shimpyo_be.domain.member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenResponseDto {

    private String grantType;
    private String accessToken;
    private long accessTokenExpiresIn;
    private String refreshToken;

    @Builder
    private TokenResponseDto(String grantType, String accessToken, long accessTokenExpiresIn,
        String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
        this.refreshToken = refreshToken;
    }
}
