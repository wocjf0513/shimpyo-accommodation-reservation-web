package com.fc.shimpyo_be.domain.reservation.unit.facade;

import com.fc.shimpyo_be.config.AbstractContainersSupport;
import com.fc.shimpyo_be.domain.reservation.dto.request.PreoccupyRoomItemRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.request.PreoccupyRoomsRequestDto;
import com.fc.shimpyo_be.domain.reservation.facade.PreoccupyRoomsLockFacade;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@SpringBootTest
public class PreoccupyRoomsLockFacadeTest extends AbstractContainersSupport {

    @Autowired
    private PreoccupyRoomsLockFacade preoccupyRoomsLockFacade;

    private static final ThreadLocal<StopWatch> threadLocalStopWatch = ThreadLocal.withInitial(StopWatch::new);

    @Test
    void checkAvailable() throws InterruptedException {
        int poolSize = 2;
        int threadSize = 5;

        ExecutorService executors = Executors.newFixedThreadPool(poolSize);
        CountDownLatch latch = new CountDownLatch(threadSize);

        PreoccupyRoomsRequestDto request1 = new PreoccupyRoomsRequestDto(List.of(
            new PreoccupyRoomItemRequestDto(1L, "2024-03-05", "2024-03-08"),
            new PreoccupyRoomItemRequestDto(2L, "2024-04-05", "2024-04-09")
        ));
        PreoccupyRoomsRequestDto request2 = new PreoccupyRoomsRequestDto(List.of(
            new PreoccupyRoomItemRequestDto(1L, "2024-03-06", "2024-03-09"),
            new PreoccupyRoomItemRequestDto(2L, "2024-04-04", "2024-04-06")
        ));
        PreoccupyRoomsRequestDto request3 = new PreoccupyRoomsRequestDto(List.of(
            new PreoccupyRoomItemRequestDto(1L, "2024-02-10", "2024-02-15"),
            new PreoccupyRoomItemRequestDto(3L, "2024-04-05", "2024-04-09")
        ));
        PreoccupyRoomsRequestDto request4 = new PreoccupyRoomsRequestDto(List.of(
            new PreoccupyRoomItemRequestDto(3L, "2024-04-04", "2024-04-08")
        ));
        PreoccupyRoomsRequestDto request5 = new PreoccupyRoomsRequestDto(List.of(
            new PreoccupyRoomItemRequestDto(5L, "2024-03-05", "2024-03-08"),
            new PreoccupyRoomItemRequestDto(6L, "2024-04-05", "2024-04-09"),
            new PreoccupyRoomItemRequestDto(7L, "2024-04-05", "2024-04-09")
        ));

        executors.submit(() -> {
            StopWatch stopWatch = threadLocalStopWatch.get();
            try {
                stopWatch.start();
                preoccupyRoomsLockFacade.checkAvailableAndPreoccupy(1L, request1);
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
                preoccupyRoomsLockFacade.checkAvailableAndPreoccupy(2L, request2);
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
                preoccupyRoomsLockFacade.checkAvailableAndPreoccupy(3L, request3);
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
                preoccupyRoomsLockFacade.checkAvailableAndPreoccupy(4L, request4);
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
                preoccupyRoomsLockFacade.checkAvailableAndPreoccupy(5L, request5);
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
}
