package com.fc.shimpyo_be.domain.reservation.unit.service;

import com.fc.shimpyo_be.domain.member.entity.Authority;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.member.exception.MemberNotFoundException;
import com.fc.shimpyo_be.domain.member.repository.MemberRepository;
import com.fc.shimpyo_be.domain.product.exception.RoomNotFoundException;
import com.fc.shimpyo_be.domain.reservation.dto.request.SaveReservationRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.response.SaveReservationResponseDto;
import com.fc.shimpyo_be.domain.reservation.entity.PayMethod;
import com.fc.shimpyo_be.domain.reservation.entity.Reservation;
import com.fc.shimpyo_be.domain.reservation.repository.ReservationRepository;
import com.fc.shimpyo_be.domain.reservation.service.ReservationService;
import com.fc.shimpyo_be.domain.reservationproduct.dto.request.ReservationProductRequestDto;
import com.fc.shimpyo_be.domain.reservationproduct.repository.ReservationProductRepository;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationProductRepository reservationProductRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    private final Member member
        = Member.builder()
        .id(1L)
        .name("member")
        .password("password")
        .authority(Authority.ROLE_USER)
        .photoUrl("photoUrl")
        .email("email")
        .build();

    @DisplayName("정상적으로 예약을 저장할 수 있다.")
    @Test
    void saveReservation_test() {
        //given
        long memberId = 1L;
        long roomId1 = 1L;
        long roomId2 = 2L;

        Room room = Room.builder()
            .id(1L)
            .name("room1")
            .description("description")
            .price(50000)
            .build();

        SaveReservationRequestDto requestDto
            = new SaveReservationRequestDto(
            List.of(
                getReservationProductRequestData(
                    roomId1, "2023-11-20", "2023-11-23",
                    "visitor1", "010-1111-1111", 150000),
                getReservationProductRequestData(
                    roomId2, "2023-11-18", "2023-11-20",
                    "visitor2", "010-2222-2222", 200000)
            ), PayMethod.CREDIT_CARD, 350000
        );

        Reservation reservation = Reservation.builder()
            .id(1L)
            .totalPrice(150000)
            .payMethod(PayMethod.CREDIT_CARD)
            .build();

        given(memberRepository.findById(anyLong()))
            .willReturn(Optional.of(member));
        given(roomRepository.findById(anyLong()))
            .willReturn(Optional.of(room));
        given(reservationRepository.save(any(Reservation.class)))
            .willReturn(reservation);

        //when
        SaveReservationResponseDto result = reservationService.saveReservation(memberId, requestDto);

        //then
        assertThat(result.reservationId()).isNotNull();

        verify(memberRepository, times(1)).findById(anyLong());
        verify(roomRepository, times(2)).findById(anyLong());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @DisplayName("회원이 존재하지 않으면 예약을 저장할 수 없다.")
    @Test
    void saveReservation_memberNotFound_test() {
        //given
        long memberId = 1L;
        long roomId1 = 1L;
        long roomId2 = 2L;

        SaveReservationRequestDto requestDto
            = new SaveReservationRequestDto(
            List.of(
                getReservationProductRequestData(
                    roomId1, "2023-11-20", "2023-11-23",
                    "visitor1", "010-1111-1111", 150000),
                getReservationProductRequestData(
                    roomId2, "2023-11-18", "2023-11-20",
                    "visitor2", "010-2222-2222", 200000)
            ), PayMethod.CREDIT_CARD, 350000
        );

        willThrow(MemberNotFoundException.class)
            .given(memberRepository).findById(anyLong());

        //when & then
        assertThatThrownBy(() -> reservationService.saveReservation(memberId, requestDto))
            .isInstanceOf(MemberNotFoundException.class);

        verify(memberRepository, times(1)).findById(anyLong());
        verify(roomRepository, times(0)).findById(anyLong());
        verify(reservationRepository, times(0)).save(any(Reservation.class));
    }

    @DisplayName("객실 정보가 존재하지 않으면 예약을 저장할 수 없다.")
    @Test
    void saveReservation_roomNotFound_test() {
        //given
        long memberId = 1L;
        long roomId1 = 1L;
        long roomId2 = 2L;

        SaveReservationRequestDto requestDto
            = new SaveReservationRequestDto(
            List.of(
                getReservationProductRequestData(
                    roomId1, "2023-11-20", "2023-11-23",
                    "visitor1", "010-1111-1111", 150000),
                getReservationProductRequestData(
                    roomId2, "2023-11-18", "2023-11-20",
                    "visitor2", "010-2222-2222", 200000)
            ), PayMethod.CREDIT_CARD, 350000
        );

        given(memberRepository.findById(anyLong()))
            .willReturn(Optional.of(member));
        willThrow(RoomNotFoundException.class)
            .given(roomRepository).findById(anyLong());

        //when & then
        assertThatThrownBy(() -> reservationService.saveReservation(memberId, requestDto))
            .isInstanceOf(RoomNotFoundException.class);

        verify(memberRepository, times(1)).findById(anyLong());
        verify(roomRepository, times(1)).findById(anyLong());
        verify(reservationRepository, times(0)).save(any(Reservation.class));
    }

    private ReservationProductRequestDto getReservationProductRequestData(
        long roomId,
        String startDate,
        String endDate,
        String visitorName,
        String visitorPhone,
        Integer price
    ) {
        String defaultValue = "DEFAULT_VALUE";
        return new ReservationProductRequestDto(
            roomId,
            defaultValue,
            defaultValue,
            2,
            4,
            startDate,
            endDate,
            defaultValue,
            defaultValue,
            visitorName,
            visitorPhone,
            price
        );
    }
}
