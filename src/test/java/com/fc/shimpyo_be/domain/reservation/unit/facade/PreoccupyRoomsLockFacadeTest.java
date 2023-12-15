package com.fc.shimpyo_be.domain.reservation.unit.facade;

import com.fc.shimpyo_be.config.AbstractContainersSupport;
import com.fc.shimpyo_be.config.DatabaseCleanUp;
import com.fc.shimpyo_be.config.TestDBCleanerConfig;
import com.fc.shimpyo_be.domain.product.entity.*;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.reservation.dto.request.PreoccupyRoomItemRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.request.PreoccupyRoomsRequestDto;
import com.fc.shimpyo_be.domain.reservation.facade.PreoccupyRoomsLockFacade;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.entity.RoomOption;
import com.fc.shimpyo_be.domain.room.entity.RoomPrice;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.util.StopWatch;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Import(TestDBCleanerConfig.class)
@SpringBootTest
public class PreoccupyRoomsLockFacadeTest extends AbstractContainersSupport {

    @Autowired
    private PreoccupyRoomsLockFacade preoccupyRoomsLockFacade;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    private final String[] tableNameArray
        = {"product", "product_option", "address", "room", "room_option", "amenity", "room_price"};

    private static final ThreadLocal<StopWatch> threadLocalStopWatch = ThreadLocal.withInitial(StopWatch::new);

    @BeforeEach
    void setUp() {
        databaseCleanUp.cleanUp(tableNameArray);

        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            String productName = "호텔" + i;
            float starAvg = ThreadLocalRandom.current().nextFloat(0, 5);
            String infoCenter = String.format("02-1234-%d%d%d%d", i, i, i, i);
            products.add(
                productRepository.save(
                    Product.builder()
                        .name(productName)
                        .thumbnail(productName + " 썸네일 url")
                        .description(productName + " 설명")
                        .starAvg(starAvg)
                        .category(Category.TOURIST_HOTEL)
                        .address(
                            Address.builder()
                                .address(productName + " 주소")
                                .detailAddress(productName + " 상세 주소")
                                .mapX(1.0)
                                .mapY(1.5)
                                .build()
                        )
                        .productOption(
                            ProductOption.builder()
                                .cooking(true)
                                .foodPlace("음료 가능")
                                .parking(true)
                                .pickup(false)
                                .infoCenter(infoCenter)
                                .build()
                        )
                        .amenity(
                            Amenity.builder()
                                .barbecue(false)
                                .beauty(true)
                                .beverage(true)
                                .fitness(true)
                                .bicycle(false)
                                .campfire(false)
                                .karaoke(true)
                                .publicBath(true)
                                .publicPc(true)
                                .seminar(false)
                                .sports(false)
                                .build()
                        )
                        .build()
                )
            );
        }

        for (int i = 1; i <= 3; i++) {
            String roomName = "객실" + i;
            roomRepository.save(
                Room.builder()
                    .product(products.get(i - 1))
                    .name(roomName)
                    .code(1000 + i)
                    .description(roomName + " 설명")
                    .standard(2)
                    .capacity(4)
                    .checkIn(LocalTime.of(14, 0))
                    .checkOut(LocalTime.of(12, 0))
                    .price(
                        RoomPrice.builder()
                            .offWeekDaysMinFee(75000)
                            .offWeekendMinFee(85000)
                            .peakWeekDaysMinFee(100000)
                            .peakWeekendMinFee(120000)
                            .build()
                    )
                    .roomOption(
                        RoomOption.builder()
                            .cooking(true)
                            .airCondition(true)
                            .bath(true)
                            .bathFacility(true)
                            .pc(false)
                            .diningTable(true)
                            .hairDryer(true)
                            .homeTheater(false)
                            .internet(true)
                            .cable(false)
                            .refrigerator(true)
                            .sofa(true)
                            .toiletries(true)
                            .tv(true)
                            .build()
                    )
                    .build()
            );
        }

        roomRepository.save(
            Room.builder()
                .product(products.get(0))
                .name("객실1")
                .code(1001)
                .description("객실1 설명")
                .standard(2)
                .capacity(4)
                .checkIn(LocalTime.of(14, 0))
                .checkOut(LocalTime.of(12, 0))
                .price(
                    RoomPrice.builder()
                        .offWeekDaysMinFee(75000)
                        .offWeekendMinFee(85000)
                        .peakWeekDaysMinFee(100000)
                        .peakWeekendMinFee(120000)
                        .build()
                )
                .roomOption(
                    RoomOption.builder()
                        .cooking(true)
                        .airCondition(true)
                        .bath(true)
                        .bathFacility(true)
                        .pc(false)
                        .diningTable(true)
                        .hairDryer(true)
                        .homeTheater(false)
                        .internet(true)
                        .cable(false)
                        .refrigerator(true)
                        .sofa(true)
                        .toiletries(true)
                        .tv(true)
                        .build()
                )
                .build()
        );
    }

    @Test
    void checkAvailable() throws InterruptedException {
        int poolSize = 2;
        int threadSize = 5;

        ExecutorService executors = Executors.newFixedThreadPool(poolSize);
        CountDownLatch latch = new CountDownLatch(threadSize);

        PreoccupyRoomsRequestDto request1 = PreoccupyRoomsRequestDto.builder()
            .rooms(
                List.of(
                    PreoccupyRoomItemRequestDto.builder()
                        .roomCode(1001L)
                        .startDate("2024-03-06")
                        .endDate("2024-03-09")
                        .build(),
                    PreoccupyRoomItemRequestDto.builder()
                        .roomCode(1002L)
                        .startDate("2024-04-04")
                        .endDate("2024-04-06")
                        .build()
                )
            )
            .build();

        PreoccupyRoomsRequestDto request2 = PreoccupyRoomsRequestDto.builder()
            .rooms(
                List.of(
                    PreoccupyRoomItemRequestDto.builder()
                        .roomCode(1001L)
                        .startDate("2024-03-05")
                        .endDate("2024-03-08")
                        .build(),
                    PreoccupyRoomItemRequestDto.builder()
                        .roomCode(1001L)
                        .startDate("2024-03-05")
                        .endDate("2024-03-08")
                        .build()
                )
            )
            .build();

        PreoccupyRoomsRequestDto request3 = PreoccupyRoomsRequestDto.builder()
            .rooms(
                List.of(
                    PreoccupyRoomItemRequestDto.builder()
                        .roomCode(1001L)
                        .startDate("2024-02-10")
                        .endDate("2024-02-15")
                        .build(),
                    PreoccupyRoomItemRequestDto.builder()
                        .roomCode(1003L)
                        .startDate("2024-04-05")
                        .endDate("2024-04-09")
                        .build()
                )
            )
            .build();

        PreoccupyRoomsRequestDto request4 = PreoccupyRoomsRequestDto.builder()
            .rooms(
                List.of(
                    PreoccupyRoomItemRequestDto.builder()
                        .roomCode(1003L)
                        .startDate("2024-04-04")
                        .endDate("2024-04-08")
                        .build()
                )
            )
            .build();

        PreoccupyRoomsRequestDto request5 = PreoccupyRoomsRequestDto.builder()
            .rooms(
                List.of(
                    PreoccupyRoomItemRequestDto.builder()
                        .roomCode(1001L)
                        .startDate("2024-02-10")
                        .endDate("2024-02-15")
                        .build(),
                    PreoccupyRoomItemRequestDto.builder()
                        .roomCode(1003L)
                        .startDate("2024-02-05")
                        .endDate("2024-02-09")
                        .build(),
                    PreoccupyRoomItemRequestDto.builder()
                        .roomCode(1002L)
                        .startDate("2024-05-10")
                        .endDate("2024-05-12")
                        .build()
                )
            )
            .build();

        executors.submit(() -> {
            StopWatch stopWatch = threadLocalStopWatch.get();
            try {
                stopWatch.start();
                preoccupyRoomsLockFacade.checkAvailableAndPreoccupy(1L, request1);
            } catch (Exception e) {
                log.error(e.getMessage());
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
                log.error(e.getMessage());
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
                log.error(e.getMessage());
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
                log.error(e.getMessage());
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
                log.error(e.getMessage());
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
