package com.fc.shimpyo_be.domain.room.controller;

import com.fc.shimpyo_be.domain.room.dto.request.GetRoomListWithProductInfoRequestDto;
import com.fc.shimpyo_be.domain.room.dto.response.RoomListWithProductInfoResponseDto;
import com.fc.shimpyo_be.domain.room.service.RoomService;
import com.fc.shimpyo_be.global.common.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
@RestController
public class RoomRestController {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<ResponseDto<RoomListWithProductInfoResponseDto>> getRoomsWithProductInfo(
        @Valid @RequestBody GetRoomListWithProductInfoRequestDto request
    ) {
        log.debug("GET /api/rooms, roomIds : {}", request.roomIds());
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                ResponseDto.res(
                    HttpStatus.OK,
                    new RoomListWithProductInfoResponseDto(
                        roomService.getRoomsWithProductInfo(request.roomIds())
                    ),
                    "숙소 정보를 포함한 객실 정보 리스트가 정상적으로 조회되었습니다."
                )
            );
    }
}
