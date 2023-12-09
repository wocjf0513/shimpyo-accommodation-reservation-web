package com.fc.shimpyo_be.domain.reservation.unit.repository;

import com.fc.shimpyo_be.config.DatabaseCleanUp;
import com.fc.shimpyo_be.config.TestDBCleanerConfig;
import com.fc.shimpyo_be.config.TestQuerydslConfig;
import com.fc.shimpyo_be.domain.member.entity.Authority;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.member.repository.MemberRepository;
import com.fc.shimpyo_be.domain.product.entity.*;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.reservation.entity.PayMethod;
import com.fc.shimpyo_be.domain.reservation.entity.Reservation;
import com.fc.shimpyo_be.domain.reservation.repository.ReservationRepository;
import com.fc.shimpyo_be.domain.reservationproduct.entity.ReservationProduct;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.entity.RoomOption;
import com.fc.shimpyo_be.domain.room.entity.RoomPrice;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({TestQuerydslConfig.class, TestDBCleanerConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class ReservationRepositoryTest {

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RoomRepository roomRepository;

    private final String[] tableNameArray = {
        "member", "product", "room", "product_option",
        "address", "room_option", "amenity", "reservation_product", "reservation"
    };

    private Member member;

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

        Product product = productRepository.save(
            Product.builder()
                .name("호텔1")
                .thumbnail("호텔1 썸네일 url")
                .description("호텔1 설명")
                .starAvg(4.0f)
                .category(Category.TOURIST_HOTEL)
                .address(
                    Address.builder()
                        .address("호텔1 주소")
                        .detailAddress("호텔1 상세 주소")
                        .mapX(10000)
                        .mapY(11000)
                        .build()
                )
                .productOption(
                    ProductOption.builder()
                        .cooking(true)
                        .foodPlace("음료 가능")
                        .parking(true)
                        .pickup(false)
                        .infoCenter("031-222-333")
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
        );

        Room room = roomRepository.save(
            Room.builder()
                .product(product)
                .name("객실1")
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

        Reservation reservation1 = reservationRepository.save(
            Reservation.builder()
                .reservationProducts(
                    List.of(
                        ReservationProduct.builder()
                            .room(room)
                            .startDate(LocalDate.of(2023, 11, 10))
                            .endDate(LocalDate.of(2023, 11, 12))
                            .visitorName("방문자명")
                            .visitorPhone("010-1111-1111")
                            .price(200000)
                            .build()
                    )
                )
                .member(member)
                .payMethod(PayMethod.CREDIT_CARD)
                .totalPrice(200000)
                .build()
        );

        Reservation reservation2 = reservationRepository.save(
            Reservation.builder()
                .reservationProducts(
                    List.of(
                        ReservationProduct.builder()
                            .room(room)
                            .startDate(LocalDate.of(2023, 12, 4))
                            .endDate(LocalDate.of(2023, 12, 7))
                            .visitorName("방문자명")
                            .visitorPhone("010-1111-1111")
                            .price(300000)
                            .build()
                    )
                )
                .totalPrice(300000)
                .payMethod(PayMethod.KAKAO_PAY)
                .member(member)
                .build()
        );
    }

    @DisplayName("findIdsByMemberId 테스트")
    @Test
    void findIdsByMemberId_test() {
        //given
        long memberId = member.getId();

        //when
        List<Long> result = reservationRepository.findIdsByMemberId(memberId);

        //then
        assertThat(result).hasSize(2);
    }
}
