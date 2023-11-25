package com.fc.shimpyo_be.domain.member.entity;

import com.fc.shimpyo_be.domain.member.dto.request.UpdateMemberRequestDto;
import com.fc.shimpyo_be.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 30)
    private String email;
    @Column(nullable = false, length = 30)
    private String name;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String photoUrl;
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public Member(Long id, String email, String name, String password, String photoUrl,
        Authority authority) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.photoUrl = photoUrl;
        this.authority = authority;
    }

    public void update(UpdateMemberRequestDto updateMemberRequestDto) {
        if (updateMemberRequestDto.getPhotoUrl() != null) {
            this.photoUrl = updateMemberRequestDto.getPhotoUrl();
        }
    }

    public void changePassword(String password){
        this.password = password;
    }
}
