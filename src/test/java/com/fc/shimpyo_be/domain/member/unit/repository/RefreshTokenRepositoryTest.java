package com.fc.shimpyo_be.domain.member.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.fc.shimpyo_be.config.TestJpaConfig;
import com.fc.shimpyo_be.config.TestQuerydslConfig;
import com.fc.shimpyo_be.domain.member.entity.RefreshToken;
import com.fc.shimpyo_be.domain.member.repository.RefreshTokenRepository;
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
public class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void reset() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        refreshTokenRepository.deleteAll();
        entityManager.createNativeQuery("TRUNCATE TABLE refresh_token").executeUpdate();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    private void saveRefreshToken() {
        RefreshToken refreshToken = RefreshToken.builder()
            .id(1L)
            .token(
                "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MDExODk5Mjh9.uZuIAxsnf4Ubz5K9YzysJTu9Gh25XNTsPVAPSElw1lS78gS8S08L97Z4RkfGodegGXZ9UFFNkVXdhRzF9Pr-u")
            .build();
        refreshTokenRepository.save(refreshToken);
    }

    @Nested
    @DisplayName("save()는")
    class Context_save {

        @Test
        @DisplayName("Refresh Token 을 저장할 수 있다.")
        void _willSuccess() {
            // given
            RefreshToken refreshToken = RefreshToken.builder()
                .id(1L)
                .token(
                    "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MDExODk5Mjh9.uZuIAxsnf4Ubz5K9YzysJTu9Gh25XNTsPVAPSElw1lS78gS8S08L97Z4RkfGodegGXZ9UFFNkVXdhRzF9Pr-u")
                .build();

            // when
            RefreshToken result = refreshTokenRepository.save(refreshToken);

            // then
            assertThat(result.getId()).isNotNull();
            assertThat(result.getToken()).isEqualTo(
                "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MDExODk5Mjh9.uZuIAxsnf4Ubz5K9YzysJTu9Gh25XNTsPVAPSElw1lS78gS8S08L97Z4RkfGodegGXZ9UFFNkVXdhRzF9Pr-u");
        }
    }

    @Nested
    @DisplayName("findById()는")
    class Context_findById {

        @Test
        @DisplayName("ID로 Refresh Token 을 조회할 수 있다.")
        void _willSuccess() {
            // given
            saveRefreshToken();

            // when
            Optional<RefreshToken> result = refreshTokenRepository.findById(1L);

            // then
            assertThat(result.isPresent()).isTrue();
            assertThat(result.get().getId()).isNotNull();
            assertThat(result.get().getToken()).isEqualTo(
                "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MDExODk5Mjh9.uZuIAxsnf4Ubz5K9YzysJTu9Gh25XNTsPVAPSElw1lS78gS8S08L97Z4RkfGodegGXZ9UFFNkVXdhRzF9Pr-u");
        }
    }
}
