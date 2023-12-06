package com.fc.shimpyo_be.domain.room.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RoomImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("객실 이미지 식별자")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "room_id")
    @Comment("객실 식별자")
    private Room room;
    @Column(nullable = false)
    @Comment("객실 이미지 URL")
    private String photoUrl;

    @Builder
    public RoomImage(Long id, Room room, String photoUrl) {
        this.id = id;
        this.room = room;
        this.photoUrl = photoUrl;
    }
}
