package com.fc.shimpyo_be.domain.room.unit.service;

import com.fc.shimpyo_be.domain.room.dto.response.RoomWithProductResponseDto;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import com.fc.shimpyo_be.domain.room.service.RoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    @DisplayName("")
    @Test
    void getRoomsWithProductInfo_test() {
        //given
        List<Long> roomIds = List.of(1L, 3L, 4L);

        List<RoomWithProductResponseDto> rooms = List.of(
            new RoomWithProductResponseDto(1L, "호텔1", "호텔1 썸네일", "호텔1 주소",
                1L, "객실1", 2, 4, "14:00", "12:00", 100000),
            new RoomWithProductResponseDto(2L, "호텔2", "호텔2 썸네일", "호텔2 주소",
                3L, "객실3", 2, 4, "14:00", "11:30", 120000),
            new RoomWithProductResponseDto(3L, "호텔3", "호텔3 썸네일", "호텔3 주소",
                4L, "객실4", 2, 4, "13:00", "11:00", 95000)
        );

        given(roomRepository.findAllInRoomIdsResponseDto(roomIds))
            .willReturn(rooms);

        //when
        List<RoomWithProductResponseDto> result = roomService.getRoomsWithProductInfo(roomIds);

        //then
        assertThat(result).hasSize(3);

        verify(roomRepository, times(1)).findAllInRoomIdsResponseDto(roomIds);
    }
}
