package com.fc.shimpyo_be.domain.favorite.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fc.shimpyo_be.domain.favorite.dto.FavoriteResponseDto;
import com.fc.shimpyo_be.domain.favorite.entity.Favorite;
import com.fc.shimpyo_be.domain.favorite.repository.FavoriteRepository;
import com.fc.shimpyo_be.domain.favorite.service.FavoriteService;
import com.fc.shimpyo_be.domain.member.entity.Authority;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.member.service.MemberService;
import com.fc.shimpyo_be.domain.product.dto.response.ProductResponse;
import com.fc.shimpyo_be.domain.product.entity.Address;
import com.fc.shimpyo_be.domain.product.entity.Amenity;
import com.fc.shimpyo_be.domain.product.entity.Category;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.entity.ProductOption;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @InjectMocks
    private FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private ProductRepository productRepository;

    @Nested
    @DisplayName("register()은")
    class Context_register {

        @Test
        @DisplayName("즐겨찾기를 등록할 수 있다.")
        void _willSuccess() {
            // given
            Member member = Member.builder()
                .id(1L)
                .email("test@mail.com")
                .name("test")
                .password("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm")
                .photoUrl(
                    "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI")
                .authority(Authority.ROLE_USER)
                .build();
            Product product = Product.builder()
                .id(1L)
                .name("OO 호텔")
                .address(Address.builder()
                    .address("서울시 강남구 OO로 000-000")
                    .detailAddress("상세주소")
                    .mapX(1.0)
                    .mapY(1.0)
                    .build())
                .thumbnail(
                    "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI")
                .category(Category.TOURIST_HOTEL)
                .starAvg(5)
                .description("호텔입니다.")
                .rooms(new ArrayList<>())
                .productOption(ProductOption.builder()
                    .cooking(false)
                    .parking(false)
                    .pickup(false)
                    .foodPlace("")
                    .infoCenter("000-0000-0000")
                    .build())
                .amenity(Amenity.builder()
                    .barbecue(false)
                    .beauty(false)
                    .beverage(false)
                    .bicycle(false)
                    .campfire(false)
                    .karaoke(false)
                    .publicBath(false)
                    .publicPc(false)
                    .sauna(false)
                    .seminar(false)
                    .sports(false)
                    .fitness(false)
                    .build())
                .build();
            Favorite favorite = Favorite.builder()
                .id(1L)
                .member(member)
                .product(product)
                .build();

            given(memberService.getMemberById(any(Long.TYPE))).willReturn(member);
            given(productRepository.findById(any(Long.TYPE))).willReturn(Optional.of(product));
            given(favoriteRepository.findByMemberAndProduct(any(Member.class), any(Product.class)))
                .willReturn(Optional.empty());
            given(favoriteRepository.save(any(Favorite.class))).willReturn(favorite);

            // when
            FavoriteResponseDto result = favoriteService.register(1L, 1L);

            // then
            assertNotNull(result);
            assertThat(result).extracting("favoriteId", "memberId", "productId")
                .containsExactly(1L, 1L, 1L);

            verify(memberService, times(1)).getMemberById(any(Long.TYPE));
            verify(productRepository, times(1)).findById(any(Long.TYPE));
            verify(favoriteRepository, times(1))
                .findByMemberAndProduct(any(Member.class), any(Product.class));
            verify(favoriteRepository, times(1)).save(any(Favorite.class));
        }
    }

    @Nested
    @DisplayName("getFavorites()은")
    class Context_getFavorites {

        @Test
        @DisplayName("즐겨찾기 목록을 조회할 수 있다.")
        void _willSuccess() {
            // given
            Member member = Member.builder()
                .id(1L)
                .email("test@mail.com")
                .name("test")
                .password("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm")
                .photoUrl(
                    "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI")
                .authority(Authority.ROLE_USER)
                .build();
            Product product = Product.builder()
                .id(1L)
                .name("OO 호텔")
                .address(Address.builder()
                    .address("서울시 강남구 OO로 000-000")
                    .detailAddress("상세주소")
                    .mapX(1.0)
                    .mapY(1.0)
                    .build())
                .thumbnail(
                    "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI")
                .category(Category.TOURIST_HOTEL)
                .starAvg(5)
                .description("호텔입니다.")
                .rooms(new ArrayList<>())
                .productOption(ProductOption.builder()
                    .cooking(false)
                    .parking(false)
                    .pickup(false)
                    .foodPlace("")
                    .infoCenter("000-0000-0000")
                    .build())
                .amenity(Amenity.builder()
                    .barbecue(false)
                    .beauty(false)
                    .beverage(false)
                    .bicycle(false)
                    .campfire(false)
                    .karaoke(false)
                    .publicBath(false)
                    .publicPc(false)
                    .sauna(false)
                    .seminar(false)
                    .sports(false)
                    .fitness(false)
                    .build())
                .build();
            Favorite favorite = Favorite.builder()
                .id(1L)
                .member(member)
                .product(product)
                .build();

            given(memberService.getMemberById(any(Long.TYPE))).willReturn(member);
            given(favoriteRepository.findAllByMember(any(Member.class))).willReturn(List.of(favorite));

            // when
            List<ProductResponse> result = favoriteService.getFavorites(1L);

            // then
            assertNotNull(result);

            verify(memberService, times(1)).getMemberById(any(Long.TYPE));
            verify(favoriteRepository, times(1)).findAllByMember(any(Member.class));
        }
    }

    @Nested
    @DisplayName("delete()은")
    class Context_delete {

        @Test
        @DisplayName("즐겨찾기를 취소할 수 있다.")
        void _willSuccess() {
            // given
            Member member = Member.builder()
                .id(1L)
                .email("test@mail.com")
                .name("test")
                .password("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm")
                .photoUrl(
                    "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI")
                .authority(Authority.ROLE_USER)
                .build();
            Product product = Product.builder()
                .id(1L)
                .name("OO 호텔")
                .address(Address.builder()
                    .address("서울시 강남구 OO로 000-000")
                    .detailAddress("상세주소")
                    .mapX(1.0)
                    .mapY(1.0)
                    .build())
                .thumbnail(
                    "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI")
                .category(Category.TOURIST_HOTEL)
                .starAvg(5)
                .description("호텔입니다.")
                .rooms(new ArrayList<>())
                .productOption(ProductOption.builder()
                    .cooking(false)
                    .parking(false)
                    .pickup(false)
                    .foodPlace("")
                    .infoCenter("000-0000-0000")
                    .build())
                .amenity(Amenity.builder()
                    .barbecue(false)
                    .beauty(false)
                    .beverage(false)
                    .bicycle(false)
                    .campfire(false)
                    .karaoke(false)
                    .publicBath(false)
                    .publicPc(false)
                    .sauna(false)
                    .seminar(false)
                    .sports(false)
                    .fitness(false)
                    .build())
                .build();
            Favorite favorite = Favorite.builder()
                .id(1L)
                .member(member)
                .product(product)
                .build();

            given(memberService.getMemberById(any(Long.TYPE))).willReturn(member);
            given(productRepository.findById(any(Long.TYPE))).willReturn(Optional.of(product));
            given(favoriteRepository.findByMemberAndProduct(any(Member.class), any(Product.class)))
                .willReturn(Optional.of(favorite));
            doNothing().when(favoriteRepository).delete(any(Favorite.class));

            // when
            FavoriteResponseDto result = favoriteService.delete(1L, 1L);

            // then
            assertNotNull(result);
            assertThat(result).extracting("favoriteId", "memberId", "productId")
                .containsExactly(1L, 1L, 1L);

            verify(memberService, times(1)).getMemberById(any(Long.TYPE));
            verify(productRepository, times(1)).findById(any(Long.TYPE));
            verify(favoriteRepository, times(1))
                .findByMemberAndProduct(any(Member.class), any(Product.class));
            verify(favoriteRepository, times(1)).delete(any(Favorite.class));
        }
    }
}
