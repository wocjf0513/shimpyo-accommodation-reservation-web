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
    @Comment("member_id")
    private Long id;
    private String token;

    @Builder
    public RefreshToken(Long id, String token) {
        this.id = id;
        this.token = token;
    }

    public void updateValue(String token) {
        this.token = token;
    }
}
