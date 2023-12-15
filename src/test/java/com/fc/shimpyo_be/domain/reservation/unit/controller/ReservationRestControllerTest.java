package com.fc.shimpyo_be.domain.reservation.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.shimpyo_be.config.AbstractContainersSupport;
import com.fc.shimpyo_be.domain.reservation.dto.request.*;
import com.fc.shimpyo_be.domain.reservation.dto.response.ReservationInfoResponseDto;
import com.fc.shimpyo_be.domain.reservation.dto.response.SaveReservationResponseDto;
import com.fc.shimpyo_be.domain.reservation.dto.response.ValidatePreoccupyResultResponseDto;
import com.fc.shimpyo_be.domain.reservation.dto.response.ValidatePreoccupyRoomResponseDto;
import com.fc.shimpyo_be.domain.reservation.entity.PayMethod;
import com.fc.shimpyo_be.domain.reservation.facade.PreoccupyRoomsLockFacade;
import com.fc.shimpyo_be.domain.reservation.facade.ReservationLockFacade;
import com.fc.shimpyo_be.domain.reservation.service.ReservationService;
import com.fc.shimpyo_be.domain.reservationproduct.dto.request.ReservationProductRequestDto;
import com.fc.shimpyo_be.domain.reservationproduct.dto.response.ReservationProductResponseDto;
import com.fc.shimpyo_be.global.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ReservationRestControllerTest extends AbstractContainersSupport {

    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private PreoccupyRoomsLockFacade preoccupyRoomsLockFacade;

    @MockBean
    private ReservationLockFacade reservationLockFacade;

    @MockBean
    private SecurityUtil securityUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(@Autowired WebApplicationContext applicationContext) {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(applicationContext)
            .apply(springSecurity())
            .alwaysDo(print())
            .build();
    }

    @WithMockUser(roles = "USER")
    @DisplayName("[api][POST][정상] 예약 주문 결제 API 성공 테스트")
    @Test
    void saveReservation_Api_test() throws Exception {
        //given
        String requestUrl = "/api/reservations";

        SaveReservationRequestDto requestDto
            = SaveReservationRequestDto.builder()
            .reservationProducts(
                List.of(
                    ReservationProductRequestDto.builder()
                        .cartId(1L)
                        .roomId(1L)
                        .startDate("2023-11-20")
                        .endDate("2023-11-23")
                        .visitorName("visitor1")
                        .visitorPhone("010-1111-1111")
                        .price(300000)
                        .build(),
                    ReservationProductRequestDto.builder()
                        .cartId(2L)
                        .roomId(3L)
                        .startDate("2023-12-10")
                        .endDate("2023-12-12")
                        .visitorName("visitor2")
                        .visitorPhone("010-2222-2222")
                        .price(150000)
                        .build()
                )
            )
            .payMethod(PayMethod.CREDIT_CARD)
            .totalPrice(450000)
            .build();

        SaveReservationResponseDto responseDto =
            SaveReservationResponseDto.builder()
                .reservationId(1L)
                .reservationProducts(
                    List.of(
                        ReservationProductResponseDto.builder()
                            .productName("숙소1")
                            .roomId(1L)
                            .roomName("객실1")
                            .standard(2)
                            .capacity(3)
                            .startDate("2023-11-20")
                            .endDate("2023-11-23")
                            .checkIn("13:00")
                            .checkOut("12:00")
                            .visitorName("visitor1")
                            .visitorPhone("010-1111-1111")
                            .price(300000)
                            .build(),
                        ReservationProductResponseDto.builder()
                            .productName("숙소2")
                            .roomId(3L)
                            .roomName("객실3")
                            .standard(2)
                            .capacity(3)
                            .startDate("2023-12-10")
                            .endDate("2023-12-12")
                            .checkIn("13:00")
                            .checkOut("12:00")
                            .visitorName("visitor2")
                            .visitorPhone("010-2222-2222")
                            .price(150000)
                            .build()
                    )
                )
                .payMethod(requestDto.payMethod())
                .totalPrice(requestDto.totalPrice())
                .createdAt("2023-12-06 10:30:35")
                .build();

        given(securityUtil.getCurrentMemberId()).willReturn(1L);
        given(reservationLockFacade.saveReservation(anyLong(), any(SaveReservationRequestDto.class)))
            .willReturn(responseDto);

        //when & then
        mockMvc.perform(
                post(requestUrl)
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data").isNotEmpty())
            .andExpect(jsonPath("$.data.reservationId").isNumber());
    }

    @WithMockUser(roles = "USER")
    @DisplayName("[api][GET][정상] 전체 주문 목록 조회 API 성공 테스트")
    @Test
    void getReservationInfoList_Api_test() throws Exception {
        //given
        String requestUrl = "/api/reservations";
        int size = 1;
        int page = 0;

        PageRequest pageRequest = PageRequest.of(page, size);
        List<ReservationInfoResponseDto> content
            = List.of(
            ReservationInfoResponseDto.builder()
                .reservationId(2L)
                .reservationProductId(3L)
                .productId(5L)
                .productName("호텔1")
                .productImageUrl("호텔1 photo URL")
                .productAddress("호텔1 주소")
                .productDetailAddress("호텔 상세 주소")
                .roomId(1L)
                .roomName("객실1")
                .startDate("2023-11-23")
                .endDate("2023-11-26")
                .checkIn("14:00")
                .checkOut("12:00")
                .price(220000)
                .payMethod("CREDIT_CARD")
                .createdAt("2023-11-20 10:00:00")
                .build()
        );

        given(securityUtil.getCurrentMemberId()).willReturn(1L);
        given(reservationService.getReservationInfoList(anyLong(), any(Pageable.class)))
            .willReturn(new PageImpl<>(content, pageRequest, 3));

        //when & then
        mockMvc.perform(
                get(requestUrl)
                    .param("size", String.valueOf(size))
                    .param("page", String.valueOf(page))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.totalPages", is(3)))
            .andExpect(jsonPath("$.data.totalElements", is(3)));
    }

    @WithMockUser(roles = "USER")
    @DisplayName("[api][POST][정상] 예약 유효성 검증 및 예약 선점 API")
    @Test
    void checkAvailableAndPreoccupy_test() throws Exception {
        // given
        String requestUrl = "/api/reservations/preoccupy";

        PreoccupyRoomsRequestDto requestDto =
            PreoccupyRoomsRequestDto.builder()
                .rooms(
                    List.of(
                        PreoccupyRoomItemRequestDto.builder()
                            .cartId(1L)
                            .roomCode(1001L)
                            .startDate("2023-12-23")
                            .endDate("2023-12-25")
                            .build(),
                        PreoccupyRoomItemRequestDto.builder()
                            .cartId(2L)
                            .roomCode(1003L)
                            .startDate("2023-11-11")
                            .endDate("2023-11-14")
                            .build()
                    )
                )
                .build();

        ValidatePreoccupyResultResponseDto responseDto =
            ValidatePreoccupyResultResponseDto.builder()
                .isAvailable(true)
                .roomResults(
                    List.of(
                        ValidatePreoccupyRoomResponseDto.builder()
                            .cartId(1L)
                            .roomCode(1001L)
                            .startDate("2023-12-23")
                            .endDate("2023-12-25")
                            .roomId(1L)
                            .build(),
                        ValidatePreoccupyRoomResponseDto.builder()
                            .cartId(2L)
                            .roomCode(1003L)
                            .startDate("2023-11-11")
                            .endDate("2023-11-14")
                            .roomId(3L)
                            .build()
                    )
                )
                .build();

        given(securityUtil.getCurrentMemberId())
            .willReturn(1L);
        given(preoccupyRoomsLockFacade.checkAvailableAndPreoccupy(1L, requestDto))
            .willReturn(responseDto);

        // when & then
        mockMvc.perform(
                post(requestUrl)
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.isAvailable", is(true)));
    }

    @WithMockUser(roles = "USER")
    @DisplayName("[api][POST][에러] 예약 유효성 검증 및 예약 선점 API - 예약 리스트 최대 갯수 초과")
    @Test
    void checkAvailableAndPreoccupy_size_validation_error_test() throws Exception {
        // given
        String requestUrl = "/api/reservations/preoccupy";

        PreoccupyRoomsRequestDto requestDto =
            PreoccupyRoomsRequestDto.builder()
                .rooms(
                    List.of(
                        PreoccupyRoomItemRequestDto.builder()
                            .cartId(1L).roomCode(1001L).startDate("2023-12-23").endDate("2023-12-25")
                            .build(),
                        PreoccupyRoomItemRequestDto.builder()
                            .cartId(2L).roomCode(1002L).startDate("2023-11-11").endDate("2023-11-14")
                            .build(),
                        PreoccupyRoomItemRequestDto.builder()
                            .cartId(3L).roomCode(1003L).startDate("2023-11-11").endDate("2023-11-14")
                            .build(),
                        PreoccupyRoomItemRequestDto.builder()
                            .cartId(4L).roomCode(1004L).startDate("2023-11-16").endDate("2023-11-18")
                            .build()
                    )
                )
                .build();

        given(securityUtil.getCurrentMemberId())
            .willReturn(1L);

        // when & then
        mockMvc.perform(
                post(requestUrl)
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code", is(400)));
    }

    @WithMockUser(roles = "USER")
    @DisplayName("[api][POST][에러] 예약 유효성 검증 및 예약 선점 API - 스트링 날짜 유효성 에러")
    @Test
    void checkAvailableAndPreoccupy_localdate_validation_error_test() throws Exception {
        // given
        String requestUrl = "/api/reservations/preoccupy";

        PreoccupyRoomsRequestDto requestDto =
            PreoccupyRoomsRequestDto.builder()
                .rooms(
                    List.of(
                        PreoccupyRoomItemRequestDto.builder()
                            .cartId(1L).roomCode(1001L).startDate("202-12-23").endDate("2023-12-25")
                            .build(),
                        PreoccupyRoomItemRequestDto.builder()
                            .cartId(2L).roomCode(1002L).startDate("2023-11-11").endDate("2023-11-14")
                            .build(),
                        PreoccupyRoomItemRequestDto.builder()
                            .cartId(3L).roomCode(1003L).startDate("2023-11-11").endDate("2023-11-14")
                            .build()
                    )
                )
                .build();

        given(securityUtil.getCurrentMemberId())
            .willReturn(1L);

        // when & then
        mockMvc.perform(
                post(requestUrl)
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code", is(400)));
    }

    @WithMockUser(roles = "USER")
    @DisplayName("[api][POST][정상] 예약 객실 선점 취소 API")
    @Test
    void releaseRooms_test() throws Exception {
        // given
        String requestUrl = "/api/reservations/release";

        ReleaseRoomsRequestDto requestDto =
            ReleaseRoomsRequestDto.builder()
                .rooms(
                    List.of(
                        ReleaseRoomItemRequestDto.builder()
                            .roomId(1L).startDate("2023-12-23").endDate("2023-12-25")
                            .build(),
                        ReleaseRoomItemRequestDto.builder()
                            .roomId(2L).startDate("2023-11-11").endDate("2023-11-14")
                            .build(),
                        ReleaseRoomItemRequestDto.builder()
                            .roomId(3L).startDate("2023-11-15").endDate("2023-11-16")
                            .build()
                    )
                )
                .build();

        given(securityUtil.getCurrentMemberId())
            .willReturn(1L);
        willDoNothing()
            .given(reservationService)
            .releaseRooms(1L, requestDto);

        // when & then
        mockMvc.perform(
                post(requestUrl)
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code", is(200)))
            .andExpect(jsonPath("$.data").isEmpty());
    }
}
