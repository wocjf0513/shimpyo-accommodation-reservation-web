package com.fc.shimpyo_be.domain.reservation.unit.service;

import com.fc.shimpyo_be.config.AbstractContainersSupport;
import com.fc.shimpyo_be.config.DatabaseCleanUp;
import com.fc.shimpyo_be.config.TestDBCleanerConfig;
import com.fc.shimpyo_be.domain.member.entity.Authority;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.member.repository.MemberRepository;
import com.fc.shimpyo_be.domain.product.entity.*;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.reservation.dto.response.ReservationInfoResponseDto;
import com.fc.shimpyo_be.domain.reservation.entity.PayMethod;
import com.fc.shimpyo_be.domain.reservation.entity.Reservation;
import com.fc.shimpyo_be.domain.reservation.repository.ReservationRepository;
import com.fc.shimpyo_be.domain.reservation.service.ReservationService;
import com.fc.shimpyo_be.domain.reservationproduct.entity.ReservationProduct;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.entity.RoomOption;
import com.fc.shimpyo_be.domain.room.entity.RoomPrice;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Import(TestDBCleanerConfig.class)
@SpringBootTest
public class GetReservationListServiceTest extends AbstractContainersSupport {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    private Member member;

    private final String[] tableNameArray = {
        "member", "product", "room", "product_option",
        "address", "room_option", "amenity", "reservation_product", "reservation"
    };

    @BeforeEach
    void setUp() {
        databaseCleanUp.cleanUp(tableNameArray);

        member = memberRepository.save(
            Member.builder()
                .email("member@email.com")
                .name("member")
                .password("password")
                .photoUrl("member photo url")
                .authority(Authority.ROLE_USER)
                .build()
        );

        List<Product> products = getProductTestDataList(3);
        List<Room> rooms = getRoomTestDataList(5, products);

        Reservation reservation1 = reservationRepository.save(
            Reservation.builder()
                .reservationProducts(
                    List.of(
                        getReservationProduct(
                            rooms.get(0),
                            LocalDate.of(2023, 11, 10),
                            LocalDate.of(2023, 11, 12),
                            200000
                        ),
                        getReservationProduct(
                            rooms.get(1),
                            LocalDate.of(2023, 11, 20),
                            LocalDate.of(2023, 11, 22),
                            200000
                        )
                    )
                )
                .member(member)
                .payMethod(PayMethod.CREDIT_CARD)
                .totalPrice(400000)
                .build()
        );

        Reservation reservation2 = reservationRepository.save(
            Reservation.builder()
                .reservationProducts(
                    List.of(
                        getReservationProduct(
                            rooms.get(2),
                            LocalDate.of(2023, 12, 4),
                            LocalDate.of(2023, 12, 7),
                            300000
                        )
                    )
                )
                .totalPrice(300000)
                .payMethod(PayMethod.KAKAO_PAY)
                .member(member)
                .build()
        );

        Reservation reservation3 = reservationRepository.save(
            Reservation.builder()
                .reservationProducts(
                    List.of(
                        getReservationProduct(
                            rooms.get(1),
                            LocalDate.of(2023, 12, 15),
                            LocalDate.of(2023, 12, 18),
                            360000
                        ),
                        getReservationProduct(
                            rooms.get(2),
                            LocalDate.of(2024, 1, 10),
                            LocalDate.of(2024, 1, 12),
                            240000
                        )
                    )
                )
                .member(member)
                .payMethod(PayMethod.CREDIT_CARD)
                .totalPrice(600000)
                .build()
        );
    }

    @DisplayName("전체 주문 목록을 정상적으로 페이징 조회할 수 있다.")
    @Test
    void getReservationInfoList_test() {
        //given
        long memberId = member.getId();
        PageRequest pageRequest = PageRequest.of(0, 2);

        //when
        Page<ReservationInfoResponseDto> result = reservationService.getReservationInfoList(memberId, pageRequest);

        //then
        assertThat(result.getTotalElements()).isEqualTo(5);
        assertThat(result.getTotalPages()).isEqualTo(3);
        log.info("{}", result.getContent().get(0));
    }

    private ReservationProduct getReservationProduct(Room room, LocalDate startDate, LocalDate endDate, int price) {
        return ReservationProduct.builder()
            .room(room)
            .startDate(startDate)
            .endDate(endDate)
            .visitorName("방문자명")
            .visitorPhone("010-1111-1111")
            .price(price)
            .build();
    }

    private List<Product> getProductTestDataList(int size) {
        List<Product> products = new ArrayList<>();

        for (int i = 1; i <= size; i++) {
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

        return products;
    }

    private List<Room> getRoomTestDataList(int size, List<Product> products) {
        List<Room> rooms = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            String roomName = "객실" + i;
            rooms.add(
                roomRepository.save(
                    Room.builder()
                        .code(1000 + i)
                        .product(products.get((i - 1) % products.size()))
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

        return rooms;
    }
}
