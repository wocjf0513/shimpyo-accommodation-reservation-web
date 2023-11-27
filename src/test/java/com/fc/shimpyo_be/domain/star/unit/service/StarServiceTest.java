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
import com.fc.shimpyo_be.domain.star.entity.Star;
import com.fc.shimpyo_be.domain.star.repository.StarRepository;
import com.fc.shimpyo_be.domain.star.service.StarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class StarServiceTest {

    @InjectMocks
    private StarService starService;

    @Mock
    private StarRepository starRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProductRepository productRepository;

    private Member member;

    private Product product;

    @BeforeEach
    void setUp() {
        member = Member.builder()
            .id(1L)
            .email("member@email.com")
            .name("member")
            .password("password")
            .photoUrl("photoUrl")
            .authority(Authority.ROLE_USER)
            .build();

        product = Product.builder()
            .id(1L)
            .name("숙소1")
            .description("숙소 설명")
            .category(Category.HOTEL)
            .thumbnail("thumbnail url")
            .starAvg(3.5f)
            .address("숙소 주소")
            .build();
    }

    @DisplayName("별점 등록에 성공한다.")
    @Test
    void register_test() {
        // given
        float score = 3.5F;
        StarRegisterRequestDto request
            = new StarRegisterRequestDto(product.getId(), score);

        given(memberRepository.findById(anyLong()))
            .willReturn(Optional.of(member));
        given(productRepository.findById(anyLong()))
            .willReturn(Optional.of(product));
        given(starRepository.save(any(Star.class)))
            .willReturn(
                Star.builder()
                    .id(1L)
                    .member(member)
                    .product(product)
                    .score(score)
                    .build()
            );

        // when
        StarResponseDto response = starService.register(member.getId(), request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.score()).isEqualTo(score);

        verify(memberRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).findById(anyLong());
        verify(starRepository, times(1)).save(any(Star.class));
    }

    @DisplayName("별점 등록시, 회원 정보가 존재하지 않을 경우 실패한다.")
    @Test
    void register_memberNotFoundException() {
        // given
        long memberId = 1000L;
        float score = 3.5F;
        StarRegisterRequestDto request
            = new StarRegisterRequestDto(product.getId(), score);

        given(memberRepository.findById(anyLong()))
            .willThrow(MemberNotFoundException.class);

        // when & then
        assertThatThrownBy(() -> starService.register(memberId, request))
            .isInstanceOf(MemberNotFoundException.class);

        verify(memberRepository, times(1)).findById(anyLong());
        verify(productRepository, times(0)).findById(anyLong());
        verify(starRepository, times(0)).save(any(Star.class));
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

        given(memberRepository.findById(anyLong()))
            .willReturn(Optional.ofNullable(member));
        given(productRepository.findById(anyLong()))
            .willThrow(ProductNotFoundException.class);

        // when & then
        assertThatThrownBy(() -> starService.register(memberId, request))
            .isInstanceOf(ProductNotFoundException.class);

        verify(memberRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).findById(anyLong());
        verify(starRepository, times(0)).save(any(Star.class));
    }
}
