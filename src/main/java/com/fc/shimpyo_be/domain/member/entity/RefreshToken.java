package com.fc.shimpyo_be.domain.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RefreshToken {

    @Id
    @Comment("Refresh 토큰 식별자(회원 식별자)")
    private Long id;
    @Comment("Refresh 토큰")
    private String token;

    @Builder
    private RefreshToken(Long id, String token) {
        this.id = id;
        this.token = token;
    }

    public void updateValue(String token) {
        this.token = token;
    }
}
