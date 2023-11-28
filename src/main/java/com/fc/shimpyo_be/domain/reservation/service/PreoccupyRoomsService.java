package com.fc.shimpyo_be.domain.reservation.service;

import com.fc.shimpyo_be.domain.reservation.dto.CheckAvailableRoomsResultDto;
import com.fc.shimpyo_be.domain.reservation.dto.request.PreoccupyRoomsRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.request.PreoccupyRoomItemRequestDto;
import com.fc.shimpyo_be.global.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class PreoccupyRoomsService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String REDIS_KEY_FORMAT = "roomId:%d:%s";

    public CheckAvailableRoomsResultDto checkAvailable(Long memberId, PreoccupyRoomsRequestDto request) {
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();

        boolean isAvailable = true;
        List<Long> unavailableIds = new LinkedList<>();
        Map<Long, Map<String, String>> recordMap = new HashMap<>();
        String memberIdValue = String.valueOf(memberId);

        for (PreoccupyRoomItemRequestDto room : request.rooms()) {

            LocalDate targetDate = DateUtil.toLocalDate(room.startDate());
            LocalDate endDate = DateUtil.toLocalDate(room.endDate());

            while(targetDate.isBefore(endDate)) {

                String key = String.format(REDIS_KEY_FORMAT, room.roomId(), targetDate);
                Object value = opsForValue.get(key);

                log.info("roomId: {}, targetDate: {}, value: {}", room.roomId(), targetDate, value);
                if(Objects.nonNull(value)) {
                    isAvailable = false;
                    unavailableIds.add(room.roomId());
                    break;
                }

                recordMap
                    .computeIfAbsent(room.roomId(), k -> new LinkedHashMap<>())
                    .put(key, memberIdValue);

                targetDate = targetDate.plusDays(1);
            }
        }

        return new CheckAvailableRoomsResultDto(isAvailable, unavailableIds, recordMap);
    }

    public void preoccupy(PreoccupyRoomsRequestDto request, Map<Long, Map<String, String>> preoccupyMap) {
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();

        for (PreoccupyRoomItemRequestDto room : request.rooms()) {
            Map<String, String> map = preoccupyMap.get(room.roomId());
            opsForValue.multiSet(map);

            Date expireDate = convertLocalDateToDate(DateUtil.toLocalDate(room.endDate()).minusDays(1));
            for (String key : map.keySet()) {
                redisTemplate.expireAt(key, expireDate);
            }
        }
    }

    private Date convertLocalDateToDate(LocalDate localDate) {
        return Date.from(
            localDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        );
    }
}
