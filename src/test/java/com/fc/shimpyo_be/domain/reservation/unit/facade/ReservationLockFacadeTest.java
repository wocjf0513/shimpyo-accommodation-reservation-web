package com.fc.shimpyo_be.domain.reservation.unit.facade;

import com.fc.shimpyo_be.config.AbstractContainersSupport;
import com.fc.shimpyo_be.config.DatabaseCleanUp;
import com.fc.shimpyo_be.config.TestDBCleanerConfig;
import com.fc.shimpyo_be.domain.member.entity.Authority;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.member.repository.MemberRepository;
import com.fc.shimpyo_be.domain.product.entity.*;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.reservation.dto.request.SaveReservationRequestDto;
import com.fc.shimpyo_be.domain.reservation.entity.PayMethod;
import com.fc.shimpyo_be.domain.reservation.facade.ReservationLockFacade;
import com.fc.shimpyo_be.domain.reservationproduct.dto.request.ReservationProductRequestDto;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Import(TestDBCleanerConfig.class)
@SpringBootTest
class ReservationLockFacadeTest extends AbstractContainersSupport {

    @Autowired
    private ReservationLockFacade reservationLockFacade;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RoomRepository roomRepository;

    private final String[] tableNameArray = {
        "member", "product", "room", "product_option", "address", "room_option", "amenity"
    };

    private static final ThreadLocal<StopWatch> threadLocalStopWatch = ThreadLocal.withInitial(StopWatch::new);

    @BeforeEach
    void setUp() {
        long memberId1 = 1L;
        long memberId2 = 2L;
        long memberId3 = 3L;
        long memberId4 = 4L;
        long memberId5 = 5L;

        databaseCleanUp.cleanUp(tableNameArray);

        for (int i = 1; i <= 5; i++) {
            String name = "member" + i;
            memberRepository.save(
                Member.builder()
                    .email(name + "@email.com")
                    .name(name)
                    .password("password")
                    .photoUrl("member photo url")
                    .authority(Authority.ROLE_USER)
                    .build()
            );
        }

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

        List<Room> rooms = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String roomName = "객실" + i;
            rooms.add(
                roomRepository.save(
                    Room.builder()
                        .product(products.get((i - 1) % 3))
                        .name(roomName)
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
                )
            );
        }

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
                new ReservationProductRequestDto(1L, 2L, "2023-11-20", "2023-11-23",
                    "visitor1", "010-1111-1111", 100000),
                new ReservationProductRequestDto(2L, 3L, "2023-11-24", "2023-11-26",
                    "visitor2", "010-2222-2222", 150000)
            ), PayMethod.KAKAO_PAY, 250000
        );

        SaveReservationRequestDto request2 = new SaveReservationRequestDto(
            List.of(new ReservationProductRequestDto(3L, 4L, "2023-10-10", "2023-10-14",
                "visitor3", "010-3333-3333", 125000)),
            PayMethod.CREDIT_CARD, 125000
        );

        SaveReservationRequestDto request3 = new SaveReservationRequestDto(
            List.of(
                new ReservationProductRequestDto(4L, 2L, "2023-11-10", "2023-11-14",
                    "visitor", "010-1111-1111", 90000),
                new ReservationProductRequestDto(5L, 5L, "2023-10-17", "2023-10-20",
                    "visitor", "010-1111-1111", 110000),
                new ReservationProductRequestDto(6L, 3L, "2023-10-10", "2023-10-14",
                    "visitor", "010-1111-1111", 100000)
            ), PayMethod.NAVER_PAY, 300000
        );

        SaveReservationRequestDto request4 = new SaveReservationRequestDto(
            List.of(
                new ReservationProductRequestDto(7L, 4L, "2023-10-05", "2023-10-07",
                    "visitor", "010-1111-1111", 125000),
                new ReservationProductRequestDto(8L, 5L, "2023-10-10", "2023-10-14",
                    "visitor", "010-1111-1111", 125000)
                ), PayMethod.KAKAO_PAY, 250000
        );

        SaveReservationRequestDto request5 = new SaveReservationRequestDto(
            List.of(
                new ReservationProductRequestDto(9L, 1L, "2023-10-10", "2023-10-14",
                    "visitor4", "010-4444-4444", 150000),
                new ReservationProductRequestDto(10L, 2L, "2023-12-11", "2023-12-15",
                    "visitor4", "010-4444-4444", 150000),
                new ReservationProductRequestDto(11L, 4L, "2023-11-11", "2023-11-14",
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
            valueOperations.set(key, String.valueOf(memberId), 60, TimeUnit.SECONDS);
            start = start.plusDays(1);
        }
    }
}
