package com.fc.shimpyo_be.domain.reservation.unit.repository;

import com.fc.shimpyo_be.config.TestQuerydslConfig;
import com.fc.shimpyo_be.domain.reservation.repository.ReservationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@Import(TestQuerydslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Sql("classpath:testdata/reservation-repository-setup.sql")
    @DisplayName("findIdsByMemberId 테스트")
    @Test
    void findIdsByMemberId_test() {
        //given
        long memberId = 1;

        //when
        List<Long> result = reservationRepository.findIdsByMemberId(memberId);

        //then
        assertThat(result).hasSize(2);
    }
}
