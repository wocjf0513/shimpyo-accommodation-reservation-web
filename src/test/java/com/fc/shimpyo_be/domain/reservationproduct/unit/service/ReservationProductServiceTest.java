package com.fc.shimpyo_be.domain.reservationproduct.unit.service;

import com.fc.shimpyo_be.domain.member.entity.Authority;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.reservation.entity.PayMethod;
import com.fc.shimpyo_be.domain.reservation.entity.Reservation;
import com.fc.shimpyo_be.domain.reservationproduct.entity.ReservationProduct;
import com.fc.shimpyo_be.domain.reservationproduct.exception.ForbiddenCancelReservationProductException;
import com.fc.shimpyo_be.domain.reservationproduct.exception.ReservationProductNotFoundException;
import com.fc.shimpyo_be.domain.reservationproduct.repository.ReservationProductRepository;
import com.fc.shimpyo_be.domain.reservationproduct.service.ReservationProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ReservationProductServiceTest {

    @InjectMocks
    private ReservationProductService reservationProductService;

    @Mock
    private ReservationProductRepository reservationProductRepository;

    @DisplayName("예약 숙소을 취소할 수 있다.")
    @Test
    void cancel_test() {
        //given
        Long id = 2L;
        Long memberId = 1L;

        Member member = Member.builder()
            .id(memberId)
            .name("member")
            .email("member@email.com")
            .authority(Authority.ROLE_USER)
            .build();

        Reservation reservation = Reservation.builder()
            .member(member)
            .payMethod(PayMethod.KAKAO_PAY)
            .totalPrice(300000)
            .build();

        int beforePrice = reservation.getTotalPrice();

        ReservationProduct reservationProduct = ReservationProduct.builder()
            .id(2L)
            .reservation(reservation)
            .price(150000)
            .startDate(LocalDate.of(2024, 2, 3))
            .endDate(LocalDate.of(2024, 2, 5))
            .build();

        given(reservationProductRepository.findByIdWithReservation(anyLong()))
            .willReturn(Optional.of(reservationProduct));

        //when
        reservationProductService.cancel(id, memberId);

        //then
        assertThat(reservation.getTotalPrice()).isEqualTo(beforePrice - reservationProduct.getPrice());
        assertThat(reservationProduct.isDeleted()).isTrue();

        verify(reservationProductRepository, times(1)).findByIdWithReservation(anyLong());
    }

    @DisplayName("예약 숙소 정보가 존재하지 않으면, 예약 숙소을 취소할 수 있다.")
    @Test
    void cancel_reservationProductNotFound_test() {
        //given
        Long id = 2L;
        Long memberId = 1L;

        given(reservationProductRepository.findByIdWithReservation(anyLong()))
            .willThrow(ReservationProductNotFoundException.class);

        //when & then
        assertThatThrownBy(() -> reservationProductService.cancel(id, memberId))
            .isInstanceOf(ReservationProductNotFoundException.class);

        verify(reservationProductRepository, times(1)).findByIdWithReservation(anyLong());
    }

    @DisplayName("예약 숙소 주문자 정보와 현재 인증 객체 정보가 일치하지 않으며, 예약 숙소을 취소할 수 없다.")
    @Test
    void cancel_forbidden_test() {
        //given
        Long id = 2L;
        Long memberId = 100L;

        Member member = Member.builder()
            .id(1L)
            .name("member")
            .email("member@email.com")
            .authority(Authority.ROLE_USER)
            .build();

        Reservation reservation = Reservation.builder()
            .member(member)
            .payMethod(PayMethod.KAKAO_PAY)
            .totalPrice(300000)
            .build();

        ReservationProduct reservationProduct = ReservationProduct.builder()
            .id(2L)
            .reservation(reservation)
            .price(150000)
            .startDate(LocalDate.of(2024, 2, 3))
            .endDate(LocalDate.of(2024, 2, 5))
            .build();

        given(reservationProductRepository.findByIdWithReservation(anyLong()))
            .willReturn(Optional.of(reservationProduct));

        //when
        assertThatThrownBy(() -> reservationProductService.cancel(id, memberId))
            .isInstanceOf(ForbiddenCancelReservationProductException.class);

        // then
        verify(reservationProductRepository, times(1)).findByIdWithReservation(anyLong());
    }
}
