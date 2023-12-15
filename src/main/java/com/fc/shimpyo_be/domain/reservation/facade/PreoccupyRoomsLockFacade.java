package com.fc.shimpyo_be.domain.reservation.facade;

import com.fc.shimpyo_be.domain.reservation.dto.CheckAvailableRoomsResultDto;
import com.fc.shimpyo_be.domain.reservation.dto.request.PreoccupyRoomsRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.response.ValidatePreoccupyResultResponseDto;
import com.fc.shimpyo_be.domain.reservation.exception.RedissonLockFailException;
import com.fc.shimpyo_be.domain.reservation.exception.PreoccupyNotAvailableException;
import com.fc.shimpyo_be.domain.reservation.service.PreoccupyRoomsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Slf4j
@RequiredArgsConstructor
@Service
public class PreoccupyRoomsLockFacade {

    private final RedissonClient redissonClient;
    private final PreoccupyRoomsService preoccupyRoomsService;

    public ValidatePreoccupyResultResponseDto checkAvailableAndPreoccupy(Long memberId, PreoccupyRoomsRequestDto request) {
        RLock lock = redissonClient.getLock("check-preoccupy");
        String currentWorker = Thread.currentThread().getName();

        try {
            boolean isLocked = lock.tryLock(2, 4, TimeUnit.SECONDS);

            if(!isLocked) {
                log.error("[{}] 체크 및 선점 lock 획득 실패", currentWorker);
                throw new RedissonLockFailException();
            }

            CheckAvailableRoomsResultDto resultDto = preoccupyRoomsService.checkAvailable(memberId, request);

            if(!resultDto.isAvailable()) {
                throw new PreoccupyNotAvailableException(
                    ValidatePreoccupyResultResponseDto.builder()
                        .isAvailable(false)
                        .roomResults(resultDto.roomResults())
                        .build()
                );
            }

            preoccupyRoomsService.preoccupy(resultDto);

            return ValidatePreoccupyResultResponseDto.builder()
                .isAvailable(true)
                .roomResults(resultDto.roomResults())
                .build();

        } catch (InterruptedException exception) {
            log.error("exception : {}, message : {}", exception.getClass().getSimpleName(), exception.getMessage());
            throw new RedissonLockFailException();
        } finally {
            lock.unlock();
        }
    }
}
