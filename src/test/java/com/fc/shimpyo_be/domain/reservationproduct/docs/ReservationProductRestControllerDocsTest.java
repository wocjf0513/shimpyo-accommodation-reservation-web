package com.fc.shimpyo_be.domain.reservationproduct.docs;

import com.fc.shimpyo_be.config.RestDocsSupport;
import com.fc.shimpyo_be.domain.reservationproduct.service.ReservationProductService;
import com.fc.shimpyo_be.global.util.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReservationProductRestControllerDocsTest extends RestDocsSupport {

    @MockBean
    private ReservationProductService reservationProductService;

    @MockBean
    private SecurityUtil securityUtil;

    @WithMockUser(roles = "USER")
    @DisplayName("cancel()는 예약 주문 상품을 취소할 수 있다.")
    @Test
    void cancel_test() throws Exception {
        // given
        String requestUrl = "/api/reservation-products/" + 1;

        given(securityUtil.getCurrentMemberId())
            .willReturn(1L);
        willDoNothing()
            .given(reservationProductService)
            .cancel(1L, 2L);

        // when
        mockMvc.perform(delete(requestUrl))
            .andExpect(status().isOk());

        verify(reservationProductService, times(1)).cancel(anyLong(), anyLong());
    }
}
