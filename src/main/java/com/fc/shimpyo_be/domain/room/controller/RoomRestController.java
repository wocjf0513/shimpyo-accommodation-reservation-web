package com.fc.shimpyo_be.domain.room.controller;

import com.fc.shimpyo_be.domain.room.dto.response.RoomListWithProductInfoResponseDto;
import com.fc.shimpyo_be.domain.room.service.RoomService;
import com.fc.shimpyo_be.global.common.ResponseDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/rooms")
@RestController
public class RoomRestController {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<ResponseDto<RoomListWithProductInfoResponseDto>> getRoomsWithProductInfo(
        @RequestParam @Size(min = 1, max = 3, message = "최소 1개, 최대 3개의 객실 식별자 정보가 필요합니다.")
        List<@Min(value = 1, message = "객실 식별자는 최소 1 이상이어야 합니다.") Long> roomIds
    ) {
        log.debug("GET /api/rooms, roomIds : {}", roomIds);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                ResponseDto.res(
                    HttpStatus.OK,
                    new RoomListWithProductInfoResponseDto(
                        roomService.getRoomsWithProductInfo(roomIds)
                    ),
                    "숙소 정보를 포함한 객실 정보 리스트가 정상적으로 조회되었습니다."
                )
            );
    }
}
