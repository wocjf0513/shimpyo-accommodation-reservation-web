package com.fc.shimpyo_be.domain.reservation.docs;

import com.fc.shimpyo_be.config.RestDocsSupport;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReservationRestControllerDocsTest extends RestDocsSupport {

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private PreoccupyRoomsLockFacade preoccupyRoomsLockFacade;

    @MockBean
    private ReservationLockFacade reservationLockFacade;

    @MockBean
    private SecurityUtil securityUtil;

    private final ConstraintDescriptions preoccupyRoomsDescriptions
        = new ConstraintDescriptions(PreoccupyRoomsRequestDto.class);

    private final ConstraintDescriptions preoccupyRoomItemDescriptions
        = new ConstraintDescriptions(PreoccupyRoomItemRequestDto.class);

    private final ConstraintDescriptions saveReservationDescriptions
        = new ConstraintDescriptions(SaveReservationRequestDto.class);

    private final ConstraintDescriptions reservationProductDescriptions
        = new ConstraintDescriptions(ReservationProductRequestDto.class);

    private final ConstraintDescriptions releaseRoomsDescriptions
        = new ConstraintDescriptions(ReleaseRoomsRequestDto.class);

    private final ConstraintDescriptions releaseRoomItemDescriptions
        = new ConstraintDescriptions(ReleaseRoomItemRequestDto.class);

    @WithMockUser(roles = "USER")
    @DisplayName("saveReservation()는 예약을 저장할 수 있다.")
    @Test
    void saveReservation() throws Exception {
        //given
        String requestUrl = "/api/reservations";

        SaveReservationRequestDto requestDto
            = SaveReservationRequestDto.builder()
            .reservationProducts(
                List.of(
                    ReservationProductRequestDto.builder()
                        .cartId(2L)
                        .roomId(1L)
                        .startDate("2023-11-20")
                        .endDate("2023-11-23")
                        .visitorName("visitor1")
                        .visitorPhone("010-1111-1111")
                        .price(300000)
                        .build(),
                    ReservationProductRequestDto.builder()
                        .cartId(7L)
                        .roomId(2L)
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

        SaveReservationResponseDto responseDto
            = SaveReservationResponseDto.builder()
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
                        .roomId(2L)
                        .roomName("객실2")
                        .standard(2)
                        .capacity(3)
                        .startDate("2023-12-10")
                        .endDate("2023-12-1")
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
        mockMvc.perform(post(requestUrl)
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(restDoc.document(
                    requestFields(
                        fieldWithPath("reservationProducts").type(JsonFieldType.ARRAY).description("예약할 객실 숙소 리스트")
                            .attributes(key("constraints").value(
                                saveReservationDescriptions.descriptionsForProperty("reservationProducts"))),
                        fieldWithPath("reservationProducts[].cartId").type(JsonFieldType.NUMBER).description("장바구니 식별자")
                            .attributes(key("constraints").value(
                                reservationProductDescriptions.descriptionsForProperty("cartId"))),
                        fieldWithPath("reservationProducts[].roomId").type(JsonFieldType.NUMBER).description("예약할 객실 식별자")
                            .attributes(key("constraints").value(
                                reservationProductDescriptions.descriptionsForProperty("roomId"))),
                        fieldWithPath("reservationProducts[].startDate").type(JsonFieldType.STRING).description("숙박 시작일")
                            .attributes(key("constraints").value(
                                reservationProductDescriptions.descriptionsForProperty("startDate"))),
                        fieldWithPath("reservationProducts[].endDate").type(JsonFieldType.STRING).description("숙박 마지막일")
                            .attributes(key("constraints").value(
                                reservationProductDescriptions.descriptionsForProperty("endDate"))),
                        fieldWithPath("reservationProducts[].visitorName").type(JsonFieldType.STRING).description("방문자명")
                            .attributes(key("constraints").value(
                                reservationProductDescriptions.descriptionsForProperty("visitorName"))),
                        fieldWithPath("reservationProducts[].visitorPhone").type(JsonFieldType.STRING).description("방문자 전화번호")
                            .attributes(key("constraints").value(
                                reservationProductDescriptions.descriptionsForProperty("visitorPhone"))),
                        fieldWithPath("reservationProducts[].price").type(JsonFieldType.NUMBER).description("객실 이용 가격")
                            .attributes(key("constraints").value(
                                reservationProductDescriptions.descriptionsForProperty("price"))),
                        fieldWithPath("payMethod").type(JsonFieldType.STRING).description("결제 수단")
                            .attributes(key("constraints").value(
                                saveReservationDescriptions.descriptionsForProperty("payMethod"))),
                        fieldWithPath("totalPrice").type(JsonFieldType.NUMBER).description("총 결제 금액")
                            .attributes(key("constraints").value(
                                saveReservationDescriptions.descriptionsForProperty("totalPrice")))

                    ),
                    responseFields(responseCommon()).and(
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.reservationId").type(JsonFieldType.NUMBER).description("예약 식별자"),
                        fieldWithPath("data.reservationProducts").type(JsonFieldType.ARRAY).description("예약 숙소 리스트"),
                        fieldWithPath("data.reservationProducts[].productName").type(JsonFieldType.STRING).description("숙소명"),
                        fieldWithPath("data.reservationProducts[].roomId").type(JsonFieldType.NUMBER).description("객실 식별자"),
                        fieldWithPath("data.reservationProducts[].roomName").type(JsonFieldType.STRING).description("객실명"),
                        fieldWithPath("data.reservationProducts[].standard").type(JsonFieldType.NUMBER).description("기준 인원"),
                        fieldWithPath("data.reservationProducts[].capacity").type(JsonFieldType.NUMBER).description("최대 인원"),
                        fieldWithPath("data.reservationProducts[].startDate").type(JsonFieldType.STRING).description("숙박 시작일"),
                        fieldWithPath("data.reservationProducts[].endDate").type(JsonFieldType.STRING).description("숙박 마지막일"),
                        fieldWithPath("data.reservationProducts[].checkIn").type(JsonFieldType.STRING).description("체크인 시간"),
                        fieldWithPath("data.reservationProducts[].checkOut").type(JsonFieldType.STRING).description("체크아웃 시간"),
                        fieldWithPath("data.reservationProducts[].visitorName").type(JsonFieldType.STRING).description("방문자명"),
                        fieldWithPath("data.reservationProducts[].visitorPhone").type(JsonFieldType.STRING).description("방문자 전화번호"),
                        fieldWithPath("data.reservationProducts[].price").type(JsonFieldType.NUMBER).description("객실 이용 가격"),
                        fieldWithPath("data.payMethod").type(JsonFieldType.STRING).description("결제 수단"),
                        fieldWithPath("data.totalPrice").type(JsonFieldType.NUMBER).description("총 결제 금액"),
                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("예약 주문 시간")
                    )
                )
            );

        verify(reservationLockFacade, times(1)).saveReservation(anyLong(), any(SaveReservationRequestDto.class));
    }

    @WithMockUser(roles = "USER")
    @DisplayName("getReservationInfoList()는 전체 주문 목록 조회를 할 수 있다.")
    @Test
    void getReservationInfoList() throws Exception {
        //given
        String requestUrl = "/api/reservations";
        int size = 1;
        int page = 0;
        PageRequest pageRequest = PageRequest.of(page, size);

        List<ReservationInfoResponseDto> content =
            List.of(
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
                        fieldWithPath("data.content[].reservationId").type(JsonFieldType.NUMBER).description("예약 식별자"),
                        fieldWithPath("data.content[].reservationProductId").type(JsonFieldType.NUMBER).description("예약 숙소 식별자"),
                        fieldWithPath("data.content[].productId").type(JsonFieldType.NUMBER).description("숙소 식별자"),
                        fieldWithPath("data.content[].productName").type(JsonFieldType.STRING).description("숙소명"),
                        fieldWithPath("data.content[].productImageUrl").type(JsonFieldType.STRING).description("숙소 대표 이미지 URL"),
                        fieldWithPath("data.content[].productAddress").type(JsonFieldType.STRING).description("숙소 주소"),
                        fieldWithPath("data.content[].productDetailAddress").type(JsonFieldType.STRING).description("숙소 상세 주소"),
                        fieldWithPath("data.content[].roomId").type(JsonFieldType.NUMBER).description("예약한 객실 식별자"),
                        fieldWithPath("data.content[].roomName").type(JsonFieldType.STRING).description("객실명"),
                        fieldWithPath("data.content[].startDate").type(JsonFieldType.STRING).description("숙박 시작일"),
                        fieldWithPath("data.content[].endDate").type(JsonFieldType.STRING).description("숙박 마지막일"),
                        fieldWithPath("data.content[].checkIn").type(JsonFieldType.STRING).description("체크인 시간"),
                        fieldWithPath("data.content[].checkOut").type(JsonFieldType.STRING).description("체크아웃 시간"),
                        fieldWithPath("data.content[].price").type(JsonFieldType.NUMBER).description("결제 금액"),
                        fieldWithPath("data.content[].payMethod").type(JsonFieldType.STRING).description("결제 수단"),
                        fieldWithPath("data.content[].createdAt").type(JsonFieldType.STRING).description("예약 결제 완료 일시"),

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
    void checkAvailableAndPreoccupy() throws Exception {
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
                            .roomCode(1002L)
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
                        fieldWithPath("rooms[].cartId").type(JsonFieldType.NUMBER).description("장바구니 식별자")
                            .attributes(key("constraints").value(
                                preoccupyRoomItemDescriptions.descriptionsForProperty("cartId"))),
                        fieldWithPath("rooms[].roomCode").type(JsonFieldType.NUMBER).description("예약할 객실 코드")
                            .attributes(key("constraints").value(
                                preoccupyRoomItemDescriptions.descriptionsForProperty("roomCode"))),
                        fieldWithPath("rooms[].startDate").type(JsonFieldType.STRING).description("숙박 시작일")
                            .attributes(key("constraints").value(
                                preoccupyRoomItemDescriptions.descriptionsForProperty("startDate"))),
                        fieldWithPath("rooms[].endDate").type(JsonFieldType.STRING).description("숙박 마지막일")
                            .attributes(key("constraints").value(
                                preoccupyRoomItemDescriptions.descriptionsForProperty("endDate")))
                    ),
                    responseFields(responseCommon()).and(
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.isAvailable").type(JsonFieldType.BOOLEAN).description("예약 선점 가능 여부"),
                        fieldWithPath("data.roomResults").type(JsonFieldType.ARRAY).description("객실별 예약 선점 가능 검증 결과 리스트"),
                        fieldWithPath("data.roomResults[].cartId").type(JsonFieldType.NUMBER).description("장바구니 식별자"),
                        fieldWithPath("data.roomResults[].roomCode").type(JsonFieldType.NUMBER).description("객실 코드"),
                        fieldWithPath("data.roomResults[].startDate").type(JsonFieldType.STRING).description("숙박 시작일"),
                        fieldWithPath("data.roomResults[].endDate").type(JsonFieldType.STRING).description("숙박 마지막일"),
                        fieldWithPath("data.roomResults[].roomId").type(JsonFieldType.NUMBER).description("매칭된 객실 식별자 (-1인 경우, 예약 선점 불가능한 객실)")
                    )
                )
            );

        verify(preoccupyRoomsLockFacade, times(1)).checkAvailableAndPreoccupy(anyLong(), any(PreoccupyRoomsRequestDto.class));
    }

    @WithMockUser(roles = "USER")
    @DisplayName("releaseRooms()는 예약 객실의 선점을 취소하고 객실을 릴리즈한다.")
    @Test
    void releaseRooms() throws Exception {
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
        mockMvc.perform(post(requestUrl)
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(restDoc.document(
                    requestFields(
                        fieldWithPath("rooms").type(JsonFieldType.ARRAY).description("선점 취소할 객실 리스트")
                            .attributes(key("constraints").value(
                                releaseRoomsDescriptions.descriptionsForProperty("rooms"))),
                        fieldWithPath("rooms[].roomId").type(JsonFieldType.NUMBER).description("선점 취소할 객실 식별자")
                            .attributes(key("constraints").value(
                                releaseRoomItemDescriptions.descriptionsForProperty("roomId"))),
                        fieldWithPath("rooms[].startDate").type(JsonFieldType.STRING).description("숙박 시작일")
                            .attributes(key("constraints").value(
                                releaseRoomItemDescriptions.descriptionsForProperty("startDate"))),
                        fieldWithPath("rooms[].endDate").type(JsonFieldType.STRING).description("숙박 마지막일")
                            .attributes(key("constraints").value(
                                releaseRoomItemDescriptions.descriptionsForProperty("endDate")))
                    ),
                    responseFields(responseCommon()).and(
                        fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터")
                    )
                )
            );

        verify(reservationService, times(1)).releaseRooms(anyLong(), any(ReleaseRoomsRequestDto.class));
    }
}
