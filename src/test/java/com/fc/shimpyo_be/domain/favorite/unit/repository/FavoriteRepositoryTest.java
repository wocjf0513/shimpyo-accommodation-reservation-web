package com.fc.shimpyo_be.domain.favorite.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.fc.shimpyo_be.config.TestJpaConfig;
import com.fc.shimpyo_be.config.TestQuerydslConfig;
import com.fc.shimpyo_be.domain.favorite.entity.Favorite;
import com.fc.shimpyo_be.domain.favorite.repository.FavoriteRepository;
import com.fc.shimpyo_be.domain.member.entity.Authority;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.member.repository.MemberRepository;
import com.fc.shimpyo_be.domain.product.entity.Address;
import com.fc.shimpyo_be.domain.product.entity.Amenity;
import com.fc.shimpyo_be.domain.product.entity.Category;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.entity.ProductOption;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@DataJpaTest
@Import({TestJpaConfig.class, TestQuerydslConfig.class})
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class FavoriteRepositoryTest {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void reset() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        favoriteRepository.deleteAll();
        memberRepository.deleteAll();
        productRepository.deleteAll();
        entityManager.createNativeQuery("TRUNCATE TABLE favorite").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE member").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE product").executeUpdate();
        entityManager
            .createNativeQuery("ALTER TABLE favorite ALTER COLUMN `id` RESTART WITH 1")
            .executeUpdate();
        entityManager
            .createNativeQuery("ALTER TABLE member ALTER COLUMN `id` RESTART WITH 1")
            .executeUpdate();
        entityManager
            .createNativeQuery("ALTER TABLE product ALTER COLUMN `id` RESTART WITH 1")
            .executeUpdate();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    private Member saveMember() {
        Member member = Member.builder()
            .email("test@mail.com")
            .name("test")
            .password("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm")
            .photoUrl(
                "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI")
            .authority(Authority.ROLE_USER)
            .build();
        return memberRepository.save(member);
    }

    private Product saveProduct() {
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
        return productRepository.save(product);
    }

    private void saveFavorite(Member member, Product product) {
        Favorite favorite = Favorite.builder()
            .member(member)
            .product(product)
            .build();
        favoriteRepository.save(favorite);
    }

    @Nested
    @DisplayName("findByMemberAndProduct()는")
    class Context_findByMemberAndProduct {

        @Test
        @DisplayName("회원과 숙소로 즐겨찾기 정보를 조회할 수 있다.")
        void _willSuccess() {
            // given
            Member member = saveMember();
            Product product = saveProduct();
            saveFavorite(member, product);

            // when
            Optional<Favorite> result = favoriteRepository.findByMemberAndProduct(member, product);

            // then
            assertThat(result.isPresent()).isTrue();
            assertThat(result.get().getId()).isNotNull();
            assertThat(result.get().getMember().getId()).isEqualTo(member.getId());
            assertThat(result.get().getProduct().getId()).isEqualTo(product.getId());
        }
    }

    @Nested
    @DisplayName("findAllByMemberId()는")
    class Context_findAllByMemberId {

        @Test
        @DisplayName("회원으로 즐겨찾기 정보 목록을 조회할 수 있다.")
        void _willSuccess() {
            // given
            Member member = saveMember();
            Product product = saveProduct();
            saveFavorite(member, product);
            Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 10);

            // when
            Page<Favorite> result = favoriteRepository.findAllByMemberId(member.getId(), pageable);

            // then
            assertThat(result.isEmpty()).isFalse();
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.get().toList().get(0).getId()).isNotNull();
            assertThat(result.get().toList().get(0).getMember().getId()).isEqualTo(member.getId());
            assertThat(result.get().toList().get(0).getProduct().getId()).isEqualTo(
                product.getId());
        }
    }
}
