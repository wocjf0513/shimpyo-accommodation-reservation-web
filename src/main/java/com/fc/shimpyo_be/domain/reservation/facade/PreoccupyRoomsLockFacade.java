package com.fc.shimpyo_be.domain.reservation.facade;

import com.fc.shimpyo_be.domain.reservation.dto.CheckAvailableRoomsResultDto;
import com.fc.shimpyo_be.domain.reservation.dto.request.PreoccupyRoomsRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.response.PreoccupyRoomsResponseDto;
import com.fc.shimpyo_be.domain.reservation.exception.RedissonLockFailException;
import com.fc.shimpyo_be.domain.reservation.exception.UnavailableRoomsException;
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


    public void checkAvailableAndPreoccupy(Long memberId, PreoccupyRoomsRequestDto request) {
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
                log.info("[{}][check available rooms result] isAvailable = {}, unavailableIds = {}", currentWorker, false, resultDto.unavailableIds());
                throw new UnavailableRoomsException(
                    new PreoccupyRoomsResponseDto(false, resultDto.unavailableIds())
                );
            }

            log.info("[{}][check available rooms result] isAvailable = {}, unavailableIds = {}", currentWorker, true, resultDto.unavailableIds());
            preoccupyRoomsService.preoccupy(request, resultDto.recordMap());

        } catch (Exception exception) {
            log.error("exception : {}, message : {}", exception.getClass().getSimpleName(), exception.getMessage());
            throw new RedissonLockFailException();
        } finally {
            lock.unlock();
        }
    }
}
