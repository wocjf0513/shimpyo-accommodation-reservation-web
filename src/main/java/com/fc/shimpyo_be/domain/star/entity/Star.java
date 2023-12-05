package com.fc.shimpyo_be.domain.star.entity;

import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.product.entity.Product;
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
public class Star {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "별점 식별자")
    private Long id;
    @Comment(value = "회원 식별자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @Comment(value = "별점 등록 상품 식별자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    @Comment(value = "별점 점수")
    @Column(nullable = false)
    private float score;

    @Builder
    public Star(Long id, Member member, Product product, float score) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.score = score;
    }
}
