package com.fc.shimpyo_be.domain.reservation.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.shimpyo_be.domain.reservation.dto.ReservationInfoResponseDto;
import com.fc.shimpyo_be.domain.reservation.service.ReservationService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ReservationRestControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private SecurityUtil securityUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup(@Autowired WebApplicationContext applicationContext) {
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
}
