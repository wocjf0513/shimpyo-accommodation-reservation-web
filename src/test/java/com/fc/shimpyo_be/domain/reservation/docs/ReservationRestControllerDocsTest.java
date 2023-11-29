package com.fc.shimpyo_be.domain.reservation.docs;

import com.fc.shimpyo_be.config.RestDocsSupport;
import com.fc.shimpyo_be.domain.reservation.dto.request.PreoccupyRoomItemRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.request.PreoccupyRoomsRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.response.PreoccupyRoomsResponseDto;
import com.fc.shimpyo_be.domain.reservation.facade.PreoccupyRoomsLockFacade;
import com.fc.shimpyo_be.domain.reservation.dto.response.ReservationInfoResponseDto;
import com.fc.shimpyo_be.domain.reservation.service.ReservationService;
import com.fc.shimpyo_be.global.util.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReservationRestControllerDocsTest extends RestDocsSupport {

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private PreoccupyRoomsLockFacade preoccupyRoomsLockFacade;

    @MockBean
    private SecurityUtil securityUtil;

    private final ConstraintDescriptions preoccupyRoomsDescriptions
        = new ConstraintDescriptions(PreoccupyRoomsRequestDto.class);

    private final ConstraintDescriptions preoccupyRoomItemDescriptions
        = new ConstraintDescriptions(PreoccupyRoomItemRequestDto.class);


    @WithMockUser(roles = "USER")
    @DisplayName("getReservationInfoList()는 전체 주문 목록 조회를 할 수 있다.")
    @Test
    void getReservationInfoList() throws Exception {
        //given
        String requestUrl = "/api/reservations";
        int size = 1;
        int page = 0;
        PageRequest pageRequest = PageRequest.of(page, size);

        List<ReservationInfoResponseDto> content
            = List.of(
            new ReservationInfoResponseDto(
                2L,
                "호텔1",
                "호텔1 photoUrl",
                "호텔1 주소 url",
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
                    .param("sort", "id,desc")
            )
            .andDo(restDoc.document(
                    queryParameters(
                        parameterWithName("page").description("현재 페이지 번호 (default = 0, 0부터 시작)"),
                        parameterWithName("size").description("한 페이지당 조회할 데이터 개수 (default = 10)"),
                        parameterWithName("sort").description("1) 정렬 기준 필드값, 2)정렬 순서 (default = id,desc)")
                    ),
                    responseFields(responseCommon()).and(
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.content").type(JsonFieldType.ARRAY).description("조회 데이터 리스트"),
                        fieldWithPath("data.content.[].reservationId").type(JsonFieldType.NUMBER).description("예약 식별자"),
                        fieldWithPath("data.content.[].productName").type(JsonFieldType.STRING).description("숙소명"),
                        fieldWithPath("data.content.[].productImageUrl").type(JsonFieldType.STRING).description("숙소 대표 이미지 URL"),
                        fieldWithPath("data.content.[].productAddress").type(JsonFieldType.STRING).description("숙소 주소"),
                        fieldWithPath("data.content.[].roomId").type(JsonFieldType.NUMBER).description("예약한 객실 식별자"),
                        fieldWithPath("data.content.[].roomName").type(JsonFieldType.STRING).description("객실명"),
                        fieldWithPath("data.content.[].startDate").type(JsonFieldType.STRING).description("체크인 날짜"),
                        fieldWithPath("data.content.[].endDate").type(JsonFieldType.STRING).description("체크아웃 날짜"),
                        fieldWithPath("data.content.[].checkIn").type(JsonFieldType.STRING).description("체크인 시간"),
                        fieldWithPath("data.content.[].checkOut").type(JsonFieldType.STRING).description("체크아웃 시간"),
                        fieldWithPath("data.content.[].price").type(JsonFieldType.NUMBER).description("결제 금액"),
                        fieldWithPath("data.content.[].payMethod").type(JsonFieldType.STRING).description("결제 수단"),

                        fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                        fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("데이터가 비었는지 여부"),
                        fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 안되어 있는지 여부"),

                        fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description("몇 번째 데이터인지 (0부터 시작)"),
                        fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                        fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description("한 페이지당 조회할 데이터 개수"),
                        fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("페이징 정보를 포함하는지 여부"),
                        fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이징 정보를 안 포함하는지 여부"),

                        fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 갯수"),
                        fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("총 데이터 갯수"),
                        fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지인지 여부"),
                        fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("첫번째 페이지인지 여부"),
                        fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("요청 페이지에서 조회된 데이터 갯수"),
                        fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                        fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("한 페이지당 조회할 데이터 갯수"),
                        fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("데이터가 비었는지 여부"),

                        fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                        fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("데이터가 비었는지 여부"),
                        fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 안되어 있는지 여부")
                    )
                )
            );
    }

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
