package com.fc.shimpyo_be.domain.reservation.unit.service;

import com.fc.shimpyo_be.config.AbstractContainersSupport;
import com.fc.shimpyo_be.domain.reservation.dto.request.ReleaseRoomItemRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.request.ReleaseRoomsRequestDto;
import com.fc.shimpyo_be.domain.reservation.service.ReservationService;
import com.fc.shimpyo_be.global.util.DateTimeUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ReleaseRoomsServiceTest extends AbstractContainersSupport {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @DisplayName("선점한 객실을 취소하고, 객실을 릴리즈 한다.")
    @Test
    void releaseRooms_test() {
        //given
        long roomId1 = 1L;
        long roomId2 = 2L;
        long memberId = 3L;

        String startDate1 = "2024-03-01";
        String endDate1 = "2024-03-04";

        String startDate2 = "2024-02-12";
        String endDate2 = "2023-02-16";

        saveRedisData(memberId, roomId1, startDate1, endDate1);
        saveRedisData(memberId, roomId2, startDate2, endDate2);

        ReleaseRoomsRequestDto requestDto = new ReleaseRoomsRequestDto(
            List.of(
                new ReleaseRoomItemRequestDto(roomId1, startDate1, endDate1),
                new ReleaseRoomItemRequestDto(roomId2, startDate2, endDate2)
            )
        );

        //when
        reservationService.releaseRooms(memberId, requestDto);

        //then
        assertThat(checkResult(roomId1, startDate1, endDate1)).isTrue();
        assertThat(checkResult(roomId2, startDate2, endDate2)).isTrue();
    }

    private void saveRedisData(long memberId, long roomId, String startDate, String endDate) {
        LocalDate targetDate = DateTimeUtil.toLocalDate(startDate);
        LocalDate lastDate = DateTimeUtil.toLocalDate(endDate);
        String keyFormat = "roomId:%d:%s";

        while (targetDate.isBefore(lastDate)) {
            redisTemplate.opsForValue()
                .set(String.format(keyFormat, roomId, targetDate), String.valueOf(memberId), 60, TimeUnit.SECONDS);

            targetDate = targetDate.plusDays(1);
        }
    }

    private boolean checkResult(long roomId, String startDate, String endDate) {
        LocalDate targetDate = DateTimeUtil.toLocalDate(startDate);
        LocalDate lastDate = DateTimeUtil.toLocalDate(endDate);
        String keyFormat = "roomId:%d:%s";

        int totalCount = 0;
        int nullCount = 0;
        while (targetDate.isBefore(lastDate)) {
            if (Objects.isNull(redisTemplate.opsForValue().get(String.format(keyFormat, roomId, targetDate)))) {
                nullCount++;
            }

            targetDate = targetDate.plusDays(1);
            totalCount++;
        }

        return totalCount == nullCount;
    }
}
