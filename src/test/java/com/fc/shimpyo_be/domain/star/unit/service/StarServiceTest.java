package com.fc.shimpyo_be.domain.star.unit.service;

import com.fc.shimpyo_be.domain.member.entity.Authority;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.member.exception.MemberNotFoundException;
import com.fc.shimpyo_be.domain.member.repository.MemberRepository;
import com.fc.shimpyo_be.domain.product.entity.Category;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.exception.ProductNotFoundException;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.star.dto.request.StarRegisterRequestDto;
import com.fc.shimpyo_be.domain.star.dto.response.StarResponseDto;
import com.fc.shimpyo_be.domain.star.repository.StarRepository;
import com.fc.shimpyo_be.domain.star.service.StarService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class StarServiceTest {

    @Autowired
    private StarService starService;

    @Autowired
    private StarRepository starRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    private Member member;

    private Product product;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(
            Member.builder()
                .email("member@email.com")
                .name("member")
                .password("password")
                .photoUrl("photoUrl")
                .authority(Authority.ROLE_USER)
                .build()
        );

        product = productRepository.save(
            Product.builder()
                .name("숙소1")
                .description("숙소 설명")
                .category(Category.HOTEL)
                .thumbnail("thumbnail url")
                .starAvg(3.5f)
                .address("숙소 주소")
                .build()
        );
    }

    @DisplayName("별점 등록에 성공한다.")
    @Test
    void register() {
        // given
        float score = 3.5F;
        long memberId = member.getId();
        StarRegisterRequestDto request
            = new StarRegisterRequestDto(product.getId(), score);

        // when
        StarResponseDto result = starService.register(memberId, request);

        // then
        assertThat(result.score()).isEqualTo(score);
    }

    @DisplayName("별점 등록시, 회원 정보가 존재하지 않을 경우 실패한다.")
    @Test
    void register_memberNotFoundException() {
        // given
        long memberId = 1000L;
        float score = 3.5F;
        StarRegisterRequestDto request
            = new StarRegisterRequestDto(product.getId(), score);

        // when & then
        assertThatThrownBy(() -> starService.register(memberId, request))
            .isInstanceOf(MemberNotFoundException.class);
    }

    @DisplayName("별점 등록시, 숙소 정보가 존재하지 않을 경우 실패한다.")
    @Test
    void register_productNotFoundException() {
        // given
        long memberId = member.getId();
        long productId = 1000L;
        float score = 4F;
        StarRegisterRequestDto request
            = new StarRegisterRequestDto(productId, score);

        // when & then
        assertThatThrownBy(() -> starService.register(memberId, request))
            .isInstanceOf(ProductNotFoundException.class);
    }

    @AfterEach
    void tearDown() {
        starRepository.deleteAll();
        memberRepository.deleteAll();
        productRepository.deleteAll();
    }
}
