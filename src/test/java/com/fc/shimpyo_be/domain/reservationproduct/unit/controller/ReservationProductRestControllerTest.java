package com.fc.shimpyo_be.domain.reservationproduct.unit.controller;

import com.fc.shimpyo_be.config.AbstractContainersSupport;
import com.fc.shimpyo_be.domain.reservationproduct.service.ReservationProductService;
import com.fc.shimpyo_be.global.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ReservationProductRestControllerTest extends AbstractContainersSupport {

    private MockMvc mockMvc;

    @MockBean
    private ReservationProductService reservationProductService;

    @MockBean
    private SecurityUtil securityUtil;

    @BeforeEach
    void setUp(@Autowired WebApplicationContext applicationContext) {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(applicationContext)
            .apply(springSecurity())
            .alwaysDo(print())
            .build();
    }

    @WithMockUser(roles = "USER")
    @DisplayName("[api][DELETE][정상] 예약 주문 상품 취소 API 테스트")
    @Test
    void saveReservation_Api_test() throws Exception {
        //given
        String requestUrl = "/api/reservation-products/" + 2;

        given(securityUtil.getCurrentMemberId()).willReturn(1L);
        willDoNothing().given(reservationProductService).cancel(anyLong(), anyLong());

        //when & then
        mockMvc.perform(
                delete(requestUrl)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code", is(200)))
            .andExpect(jsonPath("$.data").isEmpty());
    }
}
