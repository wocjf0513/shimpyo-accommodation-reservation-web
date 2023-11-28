package com.fc.shimpyo_be.domain.reservation.unit.service;

import com.fc.shimpyo_be.config.AbstractContainersSupport;
import com.fc.shimpyo_be.domain.reservation.dto.response.ReservationInfoResponseDto;
import com.fc.shimpyo_be.domain.reservation.repository.ReservationRepository;
import com.fc.shimpyo_be.domain.reservation.service.ReservationService;
import com.fc.shimpyo_be.domain.reservationproduct.repository.ReservationProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class GetReservationListServiceTest extends AbstractContainersSupport {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationProductRepository reservationProductRepository;

    @BeforeAll
    void setUp(@Autowired DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils
                .executeSqlScript(conn, new ClassPathResource("testdata/reservation-service-setup.sql"));
        }
    }

    @Sql("classpath:testdata/reservation-service-insert.sql")
    @DisplayName("전체 주문 목록을 정상적으로 페이징 조회할 수 있다.")
    @Test
    void getReservationInfoList_test() {
        //given
        long memberId = 1;
        PageRequest pageRequest = PageRequest.of(0, 2);

        //when
        Page<ReservationInfoResponseDto> result = reservationService.getReservationInfoList(memberId, pageRequest);

        //then
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(2);
        log.info("{}", result.getContent().get(0));
    }

    @AfterEach
    void tearDown() {
        reservationProductRepository.deleteAll();
        reservationRepository.deleteAll();
    }
}
