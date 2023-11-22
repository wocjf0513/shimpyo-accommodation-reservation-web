package com.fc.shimpyo_be.domain.member.dto.response;

import com.fc.shimpyo_be.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponseDto {

    private Long memberId;
    private String email;
    private String name;
    private String photoUrl;

    @Builder
    public MemberResponseDto(Long memberId, String email, String name, String photoUrl) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public static MemberResponseDto of(Member member) {
        return MemberResponseDto.builder()
            .memberId(member.getId())
            .email(member.getEmail())
            .name(member.getName())
            .photoUrl(member.getPhotoUrl())
            .build();
    }
}
