package com.fc.shimpyo_be.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckPasswordRequestDto {

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;

    @Builder
    private CheckPasswordRequestDto(String password) {
        this.password = password;
    }
}
