package com.fc.shimpyo_be.domain.reservation.service;

import com.fc.shimpyo_be.domain.reservation.dto.CheckAvailableRoomsResultDto;
import com.fc.shimpyo_be.domain.reservation.dto.request.PreoccupyRoomItemRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.request.PreoccupyRoomsRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.response.ValidatePreoccupyRoomResponseDto;
import com.fc.shimpyo_be.domain.room.service.RoomService;
import com.fc.shimpyo_be.global.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class PreoccupyRoomsService {

    private final RoomService roomService;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PREOCCUPY_REDIS_KEY_FORMAT = "roomId:%d:%s";
    private static final String CHECK_DUPLICATE_FORMAT = "%d:%s:%s";

    public CheckAvailableRoomsResultDto checkAvailable(Long memberId, PreoccupyRoomsRequestDto request) {
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();

        boolean isAvailable = true;
        String memberIdValue = String.valueOf(memberId);

        Map<Long, Map<String, String>> preoccupyMap = new HashMap<>();
        Set<String> checkSet = new HashSet<>();
        List<ValidatePreoccupyRoomResponseDto> roomResults = new ArrayList<>();

        for (PreoccupyRoomItemRequestDto room : request.rooms()) {

            LocalDate startDate = DateTimeUtil.toLocalDate(room.startDate());
            LocalDate endDate = DateTimeUtil.toLocalDate(room.endDate());
            Long cartId = room.cartId();
            Long roomCode = room.roomCode();

            List<Long> roomIds = roomService.getRoomIdsByCode(roomCode);

            boolean roomIdCheck = false;
            for (Long roomId : roomIds) {

                if(checkSet.contains(String.format(CHECK_DUPLICATE_FORMAT, roomId, startDate, endDate))) {
                    continue;
                }

                boolean dateCheck = true;
                LocalDate targetDate = startDate;
                while(targetDate.isBefore(endDate)) {

                    String key = String.format(PREOCCUPY_REDIS_KEY_FORMAT, roomId, targetDate);
                    Object value = opsForValue.get(key);

                    log.info("roomId: {}, targetDate: {}, value: {}", roomId, targetDate, value);
                    if(Objects.nonNull(value)) {
                        dateCheck = false;
                        preoccupyMap.remove(roomId);
                        break;
                    }

                    preoccupyMap
                        .computeIfAbsent(roomId, k -> new LinkedHashMap<>())
                        .put(key, memberIdValue);

                    targetDate = targetDate.plusDays(1);
                }

                if(dateCheck) {
                    roomIdCheck = true;

                    roomResults.add(
                        ValidatePreoccupyRoomResponseDto.builder()
                            .cartId(cartId)
                            .roomCode(roomCode)
                            .startDate(DateTimeUtil.toString(startDate))
                            .endDate(DateTimeUtil.toString(endDate))
                            .roomId(roomId)
                            .build()
                    );

                    checkSet.add(String.format(CHECK_DUPLICATE_FORMAT, roomId, startDate, endDate));

                    break;
                }
            }

            if (!roomIdCheck) {
                isAvailable = false;

                roomResults.add(
                    ValidatePreoccupyRoomResponseDto.builder()
                        .cartId(cartId)
                        .roomCode(roomCode)
                        .startDate(DateTimeUtil.toString(startDate))
                        .endDate(DateTimeUtil.toString(endDate))
                        .roomId(-1L)
                        .build()
                );
            }
        }

        return CheckAvailableRoomsResultDto.builder()
            .isAvailable(isAvailable)
            .roomResults(roomResults)
            .preoccupyMap(preoccupyMap)
            .build();
    }

    public void preoccupy(CheckAvailableRoomsResultDto resultDto) {
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();

        Map<Long, Map<String, String>> preoccupyMap = resultDto.preoccupyMap();

        for (ValidatePreoccupyRoomResponseDto roomResult : resultDto.roomResults()) {
            Map<String, String> map = preoccupyMap.get(roomResult.roomId());
            opsForValue.multiSet(map);

            Date expireDate = convertLocalDateTimeToDate(LocalDateTime.now().plusMinutes(35));
            for (String key : map.keySet()) {
                redisTemplate.expireAt(key, expireDate);
            }
        }
    }

    private Date convertLocalDateTimeToDate(LocalDateTime dateTime) {
        return Date.from(
            dateTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
        );
    }
}
