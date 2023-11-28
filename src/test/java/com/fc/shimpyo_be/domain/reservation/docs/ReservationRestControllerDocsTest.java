package com.fc.shimpyo_be.domain.reservation.docs;

import com.fc.shimpyo_be.config.RestDocsSupport;
import com.fc.shimpyo_be.domain.reservation.dto.request.PreoccupyRoomItemRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.request.PreoccupyRoomsRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.response.PreoccupyRoomsResponseDto;
import com.fc.shimpyo_be.domain.reservation.facade.PreoccupyRoomsLockFacade;
import com.fc.shimpyo_be.global.util.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReservationRestControllerDocsTest extends RestDocsSupport {

    @MockBean
    private PreoccupyRoomsLockFacade preoccupyRoomsLockFacade;

    @MockBean
    private SecurityUtil securityUtil;

    private final ConstraintDescriptions preoccupyRoomsDescriptions
        = new ConstraintDescriptions(PreoccupyRoomsRequestDto.class);

    private final ConstraintDescriptions preoccupyRoomItemDescriptions
        = new ConstraintDescriptions(PreoccupyRoomItemRequestDto.class);

    @WithMockUser(roles = "USER")
    @DisplayName("checkAvailableAndPreoccupy()는 객실 예약이 가능한지 검증하고, 가능한 경우 객실을 선점한다.")
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

        // when
        mockMvc.perform(post(requestUrl)
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(restDoc.document(
                    requestFields(
                        fieldWithPath("rooms").type(JsonFieldType.ARRAY).description("예약할 객실 리스트")
                            .attributes(key("constraints").value(
                                preoccupyRoomsDescriptions.descriptionsForProperty("rooms"))),
                        fieldWithPath("rooms[].roomId").type(JsonFieldType.NUMBER).description("예약할 객실 식별자")
                            .attributes(key("constraints").value(
                                preoccupyRoomItemDescriptions.descriptionsForProperty("roomId"))),
                        fieldWithPath("rooms[].startDate").type(JsonFieldType.STRING).description("숙박 시작일")
                            .attributes(key("constraints").value(
                                preoccupyRoomItemDescriptions.descriptionsForProperty("startDate"))),
                        fieldWithPath("rooms[].endDate").type(JsonFieldType.STRING).description("숙박 마지막일")
                            .attributes(key("constraints").value(
                                preoccupyRoomItemDescriptions.descriptionsForProperty("endDate")))
                    ),
                    responseFields(responseCommon()).and(
                        fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터")
                    )
                )
            );

        verify(preoccupyRoomsLockFacade, times(1)).checkAvailableAndPreoccupy(anyLong(), any(PreoccupyRoomsRequestDto.class));
    }
}
