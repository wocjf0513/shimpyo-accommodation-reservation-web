package com.fc.shimpyo_be.domain.reservation.unit.facade;

import com.fc.shimpyo_be.config.AbstractContainersSupport;
import com.fc.shimpyo_be.domain.reservation.dto.request.SaveReservationRequestDto;
import com.fc.shimpyo_be.domain.reservation.entity.PayMethod;
import com.fc.shimpyo_be.domain.reservation.facade.ReservationLockFacade;
import com.fc.shimpyo_be.domain.reservationproduct.dto.request.ReservationProductRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
class ReservationLockFacadeTest extends AbstractContainersSupport {

    @Autowired
    private ReservationLockFacade reservationLockFacade;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final ThreadLocal<StopWatch> threadLocalStopWatch = ThreadLocal.withInitial(StopWatch::new);

    @BeforeEach
    void setUp() {
        long memberId1 = 1L;
        long memberId2 = 2L;
        long memberId3 = 3L;
        long memberId4 = 4L;
        long memberId5 = 5L;

        saveRedisData(memberId1, 2L, LocalDate.of(2023, 11, 20), LocalDate.of(2023, 11, 23));
        saveRedisData(memberId1, 3L, LocalDate.of(2023, 11, 24), LocalDate.of(2023, 11, 26));

        saveRedisData(memberId2, 4L, LocalDate.of(2023, 10, 10), LocalDate.of(2023, 10, 14));

        saveRedisData(memberId3, 2L, LocalDate.of(2023, 11, 10), LocalDate.of(2023, 11, 14));
        saveRedisData(memberId3, 5L, LocalDate.of(2023, 10, 17), LocalDate.of(2023, 10, 20));
        saveRedisData(memberId3, 3L, LocalDate.of(2023, 10, 10), LocalDate.of(2023, 10, 14));

        saveRedisData(memberId4, 4L, LocalDate.of(2023, 10, 5), LocalDate.of(2023, 10, 7));
        saveRedisData(memberId4, 5L, LocalDate.of(2023, 10, 10), LocalDate.of(2023, 10, 14));

        saveRedisData(memberId5, 1L, LocalDate.of(2023, 10, 10), LocalDate.of(2023, 10, 14));
        saveRedisData(memberId5, 2L, LocalDate.of(2023, 12, 11), LocalDate.of(2023, 12, 15));
        saveRedisData(memberId5, 4L, LocalDate.of(2023, 11, 11), LocalDate.of(2023, 11, 14));
    }

    @Test
    void saveReservation_test() throws InterruptedException {
        int poolSize = 2;
        int threadSize = 5;

        ExecutorService executors = Executors.newFixedThreadPool(poolSize);
        CountDownLatch latch = new CountDownLatch(threadSize);

        SaveReservationRequestDto request1 = new SaveReservationRequestDto(
            List.of(
                new ReservationProductRequestDto(2L, "2023-11-20", "2023-11-23",
                    "visitor1", "010-1111-1111", 100000),
                new ReservationProductRequestDto(3L, "2023-11-24", "2023-11-26",
                    "visitor2", "010-2222-2222", 150000)
            ), PayMethod.KAKAO_PAY, 250000
        );

        SaveReservationRequestDto request2 = new SaveReservationRequestDto(
            List.of(new ReservationProductRequestDto(4L, "2023-10-10", "2023-10-14",
                "visitor3", "010-3333-3333", 125000)),
            PayMethod.CREDIT_CARD, 125000
        );

        SaveReservationRequestDto request3 = new SaveReservationRequestDto(
            List.of(
                new ReservationProductRequestDto(2L, "2023-11-10", "2023-11-14",
                    "visitor", "010-1111-1111", 90000),
                new ReservationProductRequestDto(5L, "2023-10-17", "2023-10-20",
                    "visitor", "010-1111-1111", 110000),
                new ReservationProductRequestDto(3L, "2023-10-10", "2023-10-14",
                    "visitor", "010-1111-1111", 100000)
            ), PayMethod.NAVER_PAY, 300000
        );

        SaveReservationRequestDto request4 = new SaveReservationRequestDto(
            List.of(
                new ReservationProductRequestDto(4L, "2023-10-05", "2023-10-07",
                    "visitor", "010-1111-1111", 125000),
                new ReservationProductRequestDto(5L, "2023-10-10", "2023-10-14",
                    "visitor", "010-1111-1111", 125000)
                ), PayMethod.KAKAO_PAY, 250000
        );

        SaveReservationRequestDto request5 = new SaveReservationRequestDto(
            List.of(
                new ReservationProductRequestDto(1L, "2023-10-10", "2023-10-14",
                    "visitor4", "010-4444-4444", 150000),
                new ReservationProductRequestDto(2L, "2023-12-11", "2023-12-15",
                    "visitor4", "010-4444-4444", 150000),
                new ReservationProductRequestDto(4L, "2023-11-11", "2023-11-14",
                    "visitor4", "010-4444-4444", 100000)
                ), PayMethod.PAYPAL, 400000
        );

        executors.submit(() -> {
            StopWatch stopWatch = threadLocalStopWatch.get();
            try {
                stopWatch.start();
                reservationLockFacade.saveReservation(1L, request1);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                stopWatch.stop();
                log.info("{} ::: {} ms", "request1", stopWatch.getTotalTimeMillis());
                threadLocalStopWatch.remove();
                latch.countDown();
            }
        });

        executors.submit(() -> {
            StopWatch stopWatch = threadLocalStopWatch.get();
            try {
                stopWatch.start();
                reservationLockFacade.saveReservation(2L, request2);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                stopWatch.stop();
                log.info("{} ::: {} ms", "request2", stopWatch.getTotalTimeMillis());
                threadLocalStopWatch.remove();
                latch.countDown();
            }
        });

        executors.submit(() -> {
            StopWatch stopWatch = threadLocalStopWatch.get();
            try {
                stopWatch.start();
                reservationLockFacade.saveReservation(3L, request3);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                stopWatch.stop();
                log.info("{} ::: {} ms", "request3", stopWatch.getTotalTimeMillis());
                threadLocalStopWatch.remove();
                latch.countDown();
            }
        });

        executors.submit(() -> {
            StopWatch stopWatch = threadLocalStopWatch.get();
            try {
                stopWatch.start();
                reservationLockFacade.saveReservation(4L, request4);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                stopWatch.stop();
                log.info("{} ::: {} ms", "request4", stopWatch.getTotalTimeMillis());
                threadLocalStopWatch.remove();
                latch.countDown();
            }
        });

        executors.submit(() -> {
            StopWatch stopWatch = threadLocalStopWatch.get();
            try {
                stopWatch.start();
                reservationLockFacade.saveReservation(5L, request5);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                stopWatch.stop();
                log.info("{} ::: {} ms", "request5", stopWatch.getTotalTimeMillis());
                threadLocalStopWatch.remove();
                latch.countDown();
            }
        });

        latch.await();
    }

    private void saveRedisData(long memberId, long roomId, LocalDate start, LocalDate end) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String keyFormat = "roomId:%d:%s";

        while (start.isBefore(end)) {
            String key = String.format(keyFormat, roomId, start);
            valueOperations.set(key, String.valueOf(memberId), 50, TimeUnit.SECONDS);
            start = start.plusDays(1);
        }
    }
}
