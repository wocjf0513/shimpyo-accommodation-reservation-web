package com.fc.shimpyo_be.domain.room.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.shimpyo_be.config.AbstractContainersSupport;
import com.fc.shimpyo_be.domain.room.dto.request.GetRoomListWithProductInfoRequestDto;
import com.fc.shimpyo_be.domain.room.dto.response.RoomWithProductResponseDto;
import com.fc.shimpyo_be.domain.room.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class RoomRestControllerTest extends AbstractContainersSupport {

    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

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
    @DisplayName("[api][GET][정상] 숙소 정보 포함 객실 정보 리스트 조회 API 테스트")
    @Test
    void getRoomsWithProductInfo_api_test() throws Exception {
        //given
        String requestUrl = "/api/rooms";
        List<Long> roomIds = List.of(1L, 3L, 4L);
        GetRoomListWithProductInfoRequestDto requestDto = new GetRoomListWithProductInfoRequestDto(roomIds);

        List<RoomWithProductResponseDto> rooms = List.of(
            RoomWithProductResponseDto.builder()
                .productId(1L)
                .productName("호텔1")
                .productThumbnail("호텔1 썸네일")
                .productAddress("호텔1 주소")
                .productDetailAddress("호텔1 상세 주소")
                .roomId(1L)
                .roomName("객실1")
                .standard(2)
                .capacity(4)
                .checkIn("14:00")
                .checkOut("12:00")
                .price(80000L)
                .build(),
            RoomWithProductResponseDto.builder()
                .productId(2L)
                .productName("호텔2")
                .productThumbnail("호텔2 썸네일")
                .productAddress("호텔2 주소")
                .productDetailAddress("호텔2 상세 주소")
                .roomId(3L)
                .roomName("객실3")
                .standard(2)
                .capacity(4)
                .checkIn("14:00")
                .checkOut("11:30")
                .price(95000L)
                .build(),
            RoomWithProductResponseDto.builder()
                .productId(3L)
                .productName("호텔3")
                .productThumbnail("호텔3 썸네일")
                .productAddress("호텔3 주소")
                .productDetailAddress("호텔3 상세 주소")
                .roomId(4L)
                .roomName("객실4")
                .standard(2)
                .capacity(4)
                .checkIn("13:00")
                .checkOut("11:00")
                .price(95000L)
                .build()
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
            .andExpect(jsonPath("$.code", is(200)))
            .andExpect(jsonPath("$.data").isNotEmpty());

        verify(roomService, times(1)).getRoomsWithProductInfo(roomIds);
    }

    @WithMockUser(roles = "USER")
    @DisplayName("[api][GET][실패] 숙소 정보 포함 객실 정보 리스트 조회 API 테스트 - 요청 데이터 검증 에러")
    @Test
    void getRoomsWithProductInfo_api_request_validation_fail_test() throws Exception {
        //given
        String requestUrl = "/api/rooms";
        List<Long> roomIds = List.of(1L, 3L, 4L, 5L);
        GetRoomListWithProductInfoRequestDto requestDto = new GetRoomListWithProductInfoRequestDto(roomIds);

        //when & then
        mockMvc.perform(
                get(requestUrl)
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
            )
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.code", is(400)));

        verify(roomService, times(0)).getRoomsWithProductInfo(roomIds);
    }
}
