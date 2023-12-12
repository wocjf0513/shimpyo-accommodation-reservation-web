package com.fc.shimpyo_be.domain.room.service;

import com.fc.shimpyo_be.domain.room.dto.response.RoomWithProductResponseDto;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import com.fc.shimpyo_be.domain.room.util.RoomMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Transactional(readOnly = true)
    public List<RoomWithProductResponseDto> getRoomsWithProductInfo(List<Long> roomIds) {
        log.debug("{} ::: {}", getClass().getSimpleName(), "getRoomsWithProductInfo");

        return roomRepository.findAllInIdsWithProductAndPrice(roomIds)
            .stream()
            .map(RoomMapper::toRoomWithProductResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<Long> getRoomIdsByCode(Long roomCode) {
        log.debug("{} ::: {}", getClass().getSimpleName(), "getRoomIdListByRoomCode");

        return roomRepository.findIdsByCode(roomCode);
    }
}
