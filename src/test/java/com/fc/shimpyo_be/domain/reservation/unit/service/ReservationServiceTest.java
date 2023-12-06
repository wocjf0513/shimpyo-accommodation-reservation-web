package com.fc.shimpyo_be.domain.reservation.unit.service;

import com.fc.shimpyo_be.config.AbstractContainersSupport;
import com.fc.shimpyo_be.domain.member.exception.MemberNotFoundException;
import com.fc.shimpyo_be.domain.product.exception.RoomNotFoundException;
import com.fc.shimpyo_be.domain.reservation.dto.request.SaveReservationRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.response.SaveReservationResponseDto;
import com.fc.shimpyo_be.domain.reservation.entity.PayMethod;
import com.fc.shimpyo_be.domain.reservation.service.ReservationService;
import com.fc.shimpyo_be.domain.reservationproduct.dto.request.ReservationProductRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
public class ReservationServiceTest extends AbstractContainersSupport {

    @Autowired
    private ReservationService reservationService;

    @Sql("classpath:testdata/reservation-service-setup.sql")
    @DisplayName("정상적으로 예약을 저장할 수 있다.")
    @Test
    void saveReservation_test() {
        //given
        long memberId = 1L;
        long roomId1 = 1L;
        long roomId2 = 2L;

        SaveReservationRequestDto requestDto
            = new SaveReservationRequestDto(
            List.of(
                new ReservationProductRequestDto(
                    roomId1, "2023-11-20", "2023-11-23",
                    "visitor1", "010-1111-1111", 150000),
                new ReservationProductRequestDto(
                    roomId2, "2023-11-26", "2023-11-30",
                    "visitor1", "010-1111-1111", 200000
                )
            ), PayMethod.CREDIT_CARD, 350000
        );

        //when
        SaveReservationResponseDto result = reservationService.saveReservation(memberId, requestDto);

        //then
        assertThat(result.reservationId()).isNotNull();
        assertThat(result.reservationProducts()).hasSize(2);
    }

    @DisplayName("회원이 존재하지 않으면 예약을 저장할 수 없다.")
    @Test
    void saveReservation_memberNotFound_test() {
        //given
        long memberId = 1000L;
        long roomId1 = 1L;
        long roomId2 = 2L;

        SaveReservationRequestDto requestDto
            = new SaveReservationRequestDto(
            List.of(
                new ReservationProductRequestDto(
                    roomId1, "2023-11-20", "2023-11-23",
                    "visitor1", "010-1111-1111", 150000),
                new ReservationProductRequestDto(
                    roomId2, "2023-11-18", "2023-11-20",
                    "visitor2", "010-2222-2222", 200000)
            ), PayMethod.CREDIT_CARD, 350000
        );

        //when & then
        assertThatThrownBy(() -> reservationService.saveReservation(memberId, requestDto))
            .isInstanceOf(MemberNotFoundException.class);
    }

    @DisplayName("객실 정보가 존재하지 않으면 예약을 저장할 수 없다.")
    @Test
    void saveReservation_roomNotFound_test() {
        //given
        long memberId = 1L;
        long roomId1 = 1L;
        long roomId2 = 2000L;

        SaveReservationRequestDto requestDto
            = new SaveReservationRequestDto(
            List.of(
                new ReservationProductRequestDto(
                    roomId1, "2023-11-20", "2023-11-23",
                    "visitor1", "010-1111-1111", 150000),
                new ReservationProductRequestDto(
                    roomId2, "2023-11-18", "2023-11-20",
                    "visitor2", "010-2222-2222", 200000)
            ), PayMethod.CREDIT_CARD, 350000
        );

        //when & then
        assertThatThrownBy(() -> reservationService.saveReservation(memberId, requestDto))
            .isInstanceOf(RoomNotFoundException.class);
    }
}
