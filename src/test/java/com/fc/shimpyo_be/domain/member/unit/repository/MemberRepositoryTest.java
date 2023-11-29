package com.fc.shimpyo_be.domain.member.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.fc.shimpyo_be.config.TestJpaConfig;
import com.fc.shimpyo_be.config.TestQuerydslConfig;
import com.fc.shimpyo_be.domain.member.entity.Authority;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

@DataJpaTest
@Import({TestJpaConfig.class, TestQuerydslConfig.class})
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void reset() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        memberRepository.deleteAll();
        entityManager.createNativeQuery("TRUNCATE TABLE member").executeUpdate();
        entityManager
            .createNativeQuery("ALTER TABLE member ALTER COLUMN `id` RESTART WITH 1")
            .executeUpdate();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    private void saveMember() {
        Member member = Member.builder()
            .email("test@mail.com")
            .name("test")
            .password("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm")
            .photoUrl(
                "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI")
            .authority(Authority.ROLE_USER)
            .build();
        memberRepository.save(member);
    }

    @Nested
    @DisplayName("save()는")
    class Context_save {

        @Test
        @DisplayName("회원 정보를 저장할 수 있다.")
        void _willSuccess() {
            // given
            Member member = Member.builder()
                .email("test@mail.com")
                .name("test")
                .password("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm")
                .photoUrl("")
                .authority(Authority.ROLE_USER)
                .build();

            // when
            Member result = memberRepository.save(member);

            // then
            assertThat(result.getId()).isNotNull();
            assertThat(result.getEmail()).isEqualTo("test@mail.com");
            assertThat(result.getName()).isEqualTo("test");
            assertThat(result.getPassword()).isEqualTo(
                "$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm");
            assertThat(result.getPhotoUrl()).isEqualTo("");
            assertThat(result.getAuthority()).isEqualTo(Authority.ROLE_USER);
        }
    }

    @Nested
    @DisplayName("findById()는")
    class Context_findById {

        @Test
        @DisplayName("ID 로 회원 정보를 조회할 수 있다.")
        void _willSuccess() {
            // given
            saveMember();

            // when
            Optional<Member> result = memberRepository.findById(1L);

            // then
            assertThat(result.isPresent()).isTrue();
            assertThat(result.get().getId()).isNotNull();
            assertThat(result.get().getEmail()).isEqualTo("test@mail.com");
            assertThat(result.get().getName()).isEqualTo("test");
            assertThat(result.get().getPassword()).isEqualTo(
                "$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm");
            assertThat(result.get().getPhotoUrl()).isEqualTo(
                "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI");
            assertThat(result.get().getAuthority()).isEqualTo(Authority.ROLE_USER);
        }
    }

    @Nested
    @DisplayName("findByEmail()는")
    class Context_findByEmail {

        @Test
        @DisplayName("Email 로 회원 정보를 조회할 수 있다.")
        void _willSuccess() {
            // given
            saveMember();

            // when
            Optional<Member> result = memberRepository.findByEmail("test@mail.com");

            // then
            assertThat(result.isPresent()).isTrue();
            assertThat(result.get().getId()).isNotNull();
            assertThat(result.get().getEmail()).isEqualTo("test@mail.com");
            assertThat(result.get().getName()).isEqualTo("test");
            assertThat(result.get().getPassword()).isEqualTo(
                "$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm");
            assertThat(result.get().getPhotoUrl()).isEqualTo(
                "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI");
            assertThat(result.get().getAuthority()).isEqualTo(Authority.ROLE_USER);
        }
    }

    @Nested
    @DisplayName("existsByEmail()는")
    class Context_existsByEmail {

        @Test
        @DisplayName("Email 에 해당하는 회원 정보 존재 여부를 확인할 수 있다.")
        void _willSuccess() {
            // given
            saveMember();

            // when
            boolean result = memberRepository.existsByEmail("test@mail.com");

            // then
            assertThat(result).isTrue();
        }
    }
}
