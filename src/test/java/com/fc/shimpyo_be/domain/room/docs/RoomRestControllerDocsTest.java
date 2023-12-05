package com.fc.shimpyo_be.domain.room.docs;

import com.fc.shimpyo_be.config.RestDocsSupport;
import com.fc.shimpyo_be.domain.room.dto.request.GetRoomListWithProductInfoRequestDto;
import com.fc.shimpyo_be.domain.room.dto.response.RoomWithProductResponseDto;
import com.fc.shimpyo_be.domain.room.service.RoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RoomRestControllerDocsTest extends RestDocsSupport {

    @MockBean
    private RoomService roomService;

    private final ConstraintDescriptions getRoomListWithProductInfoDescriptions
        = new ConstraintDescriptions(GetRoomListWithProductInfoRequestDto.class);

    @WithMockUser(roles = "USER")
    @DisplayName("getRoomsWithProductInfo()는 숙소 정보를 포함한 객실 정보 리스트를 조회할 수 있다.")
    @Test
    void getRoomsWithProductInfo() throws Exception {
        //given
        String requestUrl = "/api/rooms";
        List<Long> roomIds = List.of(1L, 3L, 4L);
        GetRoomListWithProductInfoRequestDto requestDto = new GetRoomListWithProductInfoRequestDto(roomIds);

        List<RoomWithProductResponseDto> rooms = List.of(
            new RoomWithProductResponseDto(1L, "호텔1", "호텔1 썸네일 이미지 url", 1L,
                "객실1", 2, 4, "14:00", "12:00", 100000),
            new RoomWithProductResponseDto(2L, "호텔2", "호텔2 썸네일 이미지 url", 3L,
                "객실3", 3, 5, "14:00", "11:30", 120000),
            new RoomWithProductResponseDto(3L, "호텔3", "호텔3 썸네일 이미지 url", 4L,
                "객실4", 2, 4, "13:00", "11:00", 95000)
        );

        given(roomService.getRoomsWithProductInfo(roomIds))
            .willReturn(rooms);

        //when & then
        mockMvc.perform(
                get(requestUrl)
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
            )
            .andExpect(status().isOk())
            .andDo(restDoc.document(
                    requestFields(
                        fieldWithPath("roomIds").type(JsonFieldType.ARRAY).description("조회할 객실 식별자 리스트")
                            .attributes(key("constraints").value(
                                getRoomListWithProductInfoDescriptions.descriptionsForProperty("roomIds")))
                    ),
                    responseFields(responseCommon()).and(
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.rooms").type(JsonFieldType.ARRAY).description("조회한 객실 정보 리스트"),
                        fieldWithPath("data.rooms[].productId").type(JsonFieldType.NUMBER).description("숙소 식별자"),
                        fieldWithPath("data.rooms[].productName").type(JsonFieldType.STRING).description("숙소명"),
                        fieldWithPath("data.rooms[].productThumbnail").type(JsonFieldType.STRING).description("숙소 썸네일 이미지 URL"),
                        fieldWithPath("data.rooms[].roomId").type(JsonFieldType.NUMBER).description("객실 식별자"),
                        fieldWithPath("data.rooms[].roomName").type(JsonFieldType.STRING).description("객실명"),
                        fieldWithPath("data.rooms[].standard").type(JsonFieldType.NUMBER).description("기준 인원"),
                        fieldWithPath("data.rooms[].capacity").type(JsonFieldType.NUMBER).description("최대 인원"),
                        fieldWithPath("data.rooms[].checkIn").type(JsonFieldType.STRING).description("체크인 시간"),
                        fieldWithPath("data.rooms[].checkOut").type(JsonFieldType.STRING).description("체크아웃 시간"),
                        fieldWithPath("data.rooms[].price").type(JsonFieldType.NUMBER).description("객실 가격")
                    )
                )
            );

        verify(roomService, times(1)).getRoomsWithProductInfo(roomIds);
    }
}
