package com.fc.shimpyo_be.domain.reservation.facade;

import com.fc.shimpyo_be.domain.reservation.dto.request.SaveReservationRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.response.SaveReservationResponseDto;
import com.fc.shimpyo_be.domain.reservation.dto.response.ValidationResultResponseDto;
import com.fc.shimpyo_be.domain.reservation.exception.RedissonLockFailException;
import com.fc.shimpyo_be.domain.reservation.exception.UnavailableRoomsException;
import com.fc.shimpyo_be.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationLockFacade {

    private final RedissonClient redissonClient;
    private final ReservationService reservationService;

    public SaveReservationResponseDto saveReservation(Long memberId, SaveReservationRequestDto request) {
        RLock lock = redissonClient.getLock("reservation");
        String currentWorker = Thread.currentThread().getName();

        try {
            boolean isLocked = lock.tryLock(2, 4, TimeUnit.SECONDS);

            if(!isLocked) {
                log.error("[{}] 예약 lock 획득 실패", currentWorker);
                throw new RedissonLockFailException();
            }

            ValidationResultResponseDto resultDto = reservationService.validate(memberId, request.reservationProducts());

            if(!resultDto.isAvailable()) {
                log.info("[{}][validate rooms result] isAvailable = {}, unavailableIds = {}", currentWorker, false, resultDto.unavailableIds());
                throw new UnavailableRoomsException(
                    new ValidationResultResponseDto(false, resultDto.unavailableIds())
                );
            }
            log.info("[{}][validate rooms result] isAvailable = {}, unavailableIds = {}", currentWorker, true, resultDto.unavailableIds());

            return reservationService.saveReservation(memberId, request);

        } catch (InterruptedException exception) {
            log.error("exception : {}, message : {}", exception.getClass().getSimpleName(), exception.getMessage());
            throw new RedissonLockFailException();
        } finally {
            lock.unlock();
        }
    }
}
