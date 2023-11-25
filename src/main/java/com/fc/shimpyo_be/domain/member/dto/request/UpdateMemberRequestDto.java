package com.fc.shimpyo_be.domain.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateMemberRequestDto {

    private String password;
    private String passwordConfirm;
    private String photoUrl;

    @Builder
    public UpdateMemberRequestDto(String password, String passwordConfirm, String photoUrl) {
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.photoUrl = photoUrl;
    }
}
