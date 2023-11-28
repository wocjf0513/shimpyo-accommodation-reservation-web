package com.fc.shimpyo_be.domain.reservation.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.shimpyo_be.domain.reservation.dto.response.ReservationInfoResponseDto;
import com.fc.shimpyo_be.domain.reservation.service.ReservationService;
import com.fc.shimpyo_be.config.AbstractContainersSupport;
import com.fc.shimpyo_be.domain.reservation.dto.request.PreoccupyRoomItemRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.request.PreoccupyRoomsRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.response.PreoccupyRoomsResponseDto;
import com.fc.shimpyo_be.domain.reservation.facade.PreoccupyRoomsLockFacade;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
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
    @DisplayName("[api][GET] 전체 주문 목록 조회 API 성공 테스트")
    @Test
    void getReservationInfoList_Api_test() throws Exception {
        //given
        String requestUrl = "/api/reservations";
        int size = 1;
        int page = 0;

        PageRequest pageRequest = PageRequest.of(page, size);
        List<ReservationInfoResponseDto> content
            = List.of(
            new ReservationInfoResponseDto(
                2L,
                "호텔2",
                "호텔 photoUrl",
                "호텔 주소 url",
                1L,
                "객실1",
                "2023-11-23",
                "2023-11-26",
                "14:00",
                "12:00",
                220000,
                "CREDIT_CARD"
            )
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

    @DisplayName("[api][POST][정상] 예약 유효성 검증 및 예약 선점 API")
    @Test
    void checkAvailableAndPreoccupy_test() throws Exception {
        // given
        String requestUrl = "/api/reservations/preoccupy";

        PreoccupyRoomsRequestDto requestDto
            = new PreoccupyRoomsRequestDto(
                List.of(
                    new PreoccupyRoomItemRequestDto(1L, "2023-12-23", "2023-12-25"),
                    new PreoccupyRoomItemRequestDto(2L, "2023-11-11", "2023-11-14")
                )
        );

        PreoccupyRoomsResponseDto responseDto
            = new PreoccupyRoomsResponseDto(true, new ArrayList<>());

        given(securityUtil.getCurrentMemberId())
            .willReturn(1L);
        willDoNothing()
            .given(preoccupyRoomsLockFacade)
            .checkAvailableAndPreoccupy(1L, requestDto);

        // when & then
        mockMvc.perform(
            post(requestUrl)
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @WithMockUser(roles = "USER")
    @DisplayName("[api][POST][에러] 예약 유효성 검증 및 예약 선점 API - 예약 리스트 최대 갯수 초과")
    @Test
    void checkAvailableAndPreoccupy_size_validation_error_test() throws Exception {
        // given
        String requestUrl = "/api/reservations/preoccupy";

        PreoccupyRoomsRequestDto requestDto
            = new PreoccupyRoomsRequestDto(
            List.of(
                new PreoccupyRoomItemRequestDto(1L, "2023-12-23", "2023-12-25"),
                new PreoccupyRoomItemRequestDto(2L, "2023-11-11", "2023-11-14"),
                new PreoccupyRoomItemRequestDto(3L, "2023-11-11", "2023-11-14"),
                new PreoccupyRoomItemRequestDto(4L, "2023-11-11", "2023-11-14")
            )
        );

        PreoccupyRoomsResponseDto responseDto
            = new PreoccupyRoomsResponseDto(true, new ArrayList<>());

        given(securityUtil.getCurrentMemberId())
            .willReturn(1L);
        willDoNothing()
            .given(preoccupyRoomsLockFacade)
            .checkAvailableAndPreoccupy(1L, requestDto);

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

        PreoccupyRoomsRequestDto requestDto
            = new PreoccupyRoomsRequestDto(
            List.of(
                new PreoccupyRoomItemRequestDto(1L, "202-12-23", "2023-12-25"),
                new PreoccupyRoomItemRequestDto(2L, "2023-11-11", "2023-11-14"),
                new PreoccupyRoomItemRequestDto(3L, "2023-11-11", "2023-11-14")
            )
        );

        PreoccupyRoomsResponseDto responseDto
            = new PreoccupyRoomsResponseDto(true, new ArrayList<>());

        given(securityUtil.getCurrentMemberId())
            .willReturn(1L);
        willDoNothing()
            .given(preoccupyRoomsLockFacade)
            .checkAvailableAndPreoccupy(1L, requestDto);

        // when & then
        mockMvc.perform(
                post(requestUrl)
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code", is(400)));
    }
}
