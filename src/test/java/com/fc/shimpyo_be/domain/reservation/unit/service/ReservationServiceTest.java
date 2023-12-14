package com.fc.shimpyo_be.domain.reservation.unit.service;

import com.fc.shimpyo_be.config.AbstractContainersSupport;
import com.fc.shimpyo_be.config.DatabaseCleanUp;
import com.fc.shimpyo_be.config.TestDBCleanerConfig;
import com.fc.shimpyo_be.domain.cart.entity.Cart;
import com.fc.shimpyo_be.domain.cart.repository.CartRepository;
import com.fc.shimpyo_be.domain.member.entity.Authority;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.member.exception.MemberNotFoundException;
import com.fc.shimpyo_be.domain.member.repository.MemberRepository;
import com.fc.shimpyo_be.domain.product.entity.*;
import com.fc.shimpyo_be.domain.product.exception.RoomNotFoundException;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.reservation.dto.request.SaveReservationRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.response.SaveReservationResponseDto;
import com.fc.shimpyo_be.domain.reservation.entity.PayMethod;
import com.fc.shimpyo_be.domain.reservation.service.ReservationService;
import com.fc.shimpyo_be.domain.reservationproduct.dto.request.ReservationProductRequestDto;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.entity.RoomOption;
import com.fc.shimpyo_be.domain.room.entity.RoomPrice;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import com.fc.shimpyo_be.global.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@Import(TestDBCleanerConfig.class)
@SpringBootTest
public class ReservationServiceTest extends AbstractContainersSupport {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    private Member member;

    private final String[] tableNameArray = {
        "member", "product", "room", "product_option", "address", "room_option", "amenity", "cart"
    };

    private LocalDate startDate1 = LocalDate.now().plusDays(1);
    private LocalDate endDate1 = startDate1.plusDays(2);

    private LocalDate startDate2 = LocalDate.now().plusMonths(2);
    private LocalDate endDate2 = startDate2.plusDays(3);

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
        for (int i = 1; i <= 3; i++) {
            String roomName = "객실" + i;
            rooms.add(
                roomRepository.save(
                    Room.builder()
                        .code(1000 + i)
                        .product(products.get(i - 1))
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

        cartRepository.save(
            Cart.builder()
                .member(member)
                .roomCode(rooms.get(0).getCode())
                .startDate(startDate1)
                .endDate(endDate1)
                .price(150000L)
                .build()
        );

        cartRepository.save(
            Cart.builder()
                .member(member)
                .roomCode(rooms.get(1).getCode())
                .startDate(startDate2)
                .endDate(endDate2)
                .price(200000L)
                .build()
        );
    }

    @DisplayName("정상적으로 예약을 저장할 수 있다.")
    @Test
    void saveReservation_test() {
        //given
        long memberId = member.getId();
        long roomId1 = 1L;
        long roomId2 = 2L;

        SaveReservationRequestDto requestDto
            = new SaveReservationRequestDto(
            List.of(
                new ReservationProductRequestDto(
                    1L, roomId1, startDate1.toString(), endDate1.toString(),
                    "visitor1", "010-1111-1111", 150000),
                new ReservationProductRequestDto(
                    2L, roomId2, startDate2.toString(), endDate2.toString(),
                    "visitor1", "010-1111-1111", 200000
                )
            ), PayMethod.CREDIT_CARD, 350000
        );

        Map<Long, List<String>> map = new HashMap<>();
        for (ReservationProductRequestDto reservationProduct : requestDto.reservationProducts()) {
            map.put(
                reservationProduct.roomId(),
                getKeyList(reservationProduct.roomId(), reservationProduct.startDate(), reservationProduct.endDate())
            );
        }

        //when
        SaveReservationResponseDto result = reservationService.saveReservation(memberId, requestDto, map);

        //then
        assertThat(result.reservationId()).isNotNull();
        assertThat(result.reservationProducts()).hasSize(2);
        assertThat(cartRepository.findById(1L)).isNotPresent();
        assertThat(cartRepository.findById(2L)).isNotPresent();
    }

    @DisplayName("예약 저장시 장바구니 식별자가 -1인 경우는 장바구니 아이템을 삭제하지 않는다.")
    @Test
    void saveReservation_cart_delete_filter_test() {
        //given
        long memberId = member.getId();
        long roomId1 = 1L;
        long cartId1 = -1L;

        SaveReservationRequestDto requestDto
            = new SaveReservationRequestDto(
            List.of(
                new ReservationProductRequestDto(
                    cartId1, roomId1, startDate1.toString(), endDate1.toString(),
                    "visitor1", "010-1111-1111", 150000)
            ), PayMethod.CREDIT_CARD, 150000
        );

        Map<Long, List<String>> map = new HashMap<>();
        for (ReservationProductRequestDto reservationProduct : requestDto.reservationProducts()) {
            map.put(
                reservationProduct.roomId(),
                getKeyList(reservationProduct.roomId(), reservationProduct.startDate(), reservationProduct.endDate())
            );
        }

        //when
        SaveReservationResponseDto result = reservationService.saveReservation(memberId, requestDto, map);

        //then
        assertThat(result.reservationId()).isNotNull();
        assertThat(result.reservationProducts()).hasSize(1);
        assertThat(cartRepository.findAll()).hasSize(2);
    }

    @DisplayName("회원이 존재하지 않으면 예약을 저장할 수 없다.")
    @Test
    void saveReservation_memberNotFound_test() {
        //given
        long memberId = 1000L;
        long roomId1 = 1L;
        long roomId2 = 2L;

        SaveReservationRequestDto requestDto
            = new SaveReservationRequestDto(
            List.of(
                new ReservationProductRequestDto(
                    1L, roomId1, startDate1.toString(), endDate1.toString(),
                    "visitor1", "010-1111-1111", 150000),
                new ReservationProductRequestDto(
                    2L, roomId2, startDate2.toString(), endDate2.toString(),
                    "visitor1", "010-1111-1111", 200000
                )
            ), PayMethod.CREDIT_CARD, 350000
        );

        Map<Long, List<String>> map = new HashMap<>();
        for (ReservationProductRequestDto reservationProduct : requestDto.reservationProducts()) {
            map.put(
                reservationProduct.roomId(),
                getKeyList(reservationProduct.roomId(), reservationProduct.startDate(), reservationProduct.endDate())
            );
        }

        //when & then
        assertThatThrownBy(() -> reservationService.saveReservation(memberId, requestDto, map))
            .isInstanceOf(MemberNotFoundException.class);
    }

    @DisplayName("객실 정보가 존재하지 않으면 예약을 저장할 수 없다.")
    @Test
    void saveReservation_roomNotFound_test() {
        //given
        long memberId = member.getId();
        long roomId1 = 1L;
        long roomId2 = 2000L;

        SaveReservationRequestDto requestDto
            = new SaveReservationRequestDto(
            List.of(
                new ReservationProductRequestDto(
                    1L, roomId1, startDate1.toString(), endDate1.toString(),
                    "visitor1", "010-1111-1111", 150000),
                new ReservationProductRequestDto(
                    2L, roomId2, startDate2.toString(), endDate2.toString(),
                    "visitor1", "010-1111-1111", 200000
                )
            ), PayMethod.CREDIT_CARD, 350000
        );

        Map<Long, List<String>> map = new HashMap<>();
        for (ReservationProductRequestDto reservationProduct : requestDto.reservationProducts()) {
            map.put(
                reservationProduct.roomId(),
                getKeyList(reservationProduct.roomId(), reservationProduct.startDate(), reservationProduct.endDate())
            );
        }

        //when & then
        assertThatThrownBy(() -> reservationService.saveReservation(memberId, requestDto, map))
            .isInstanceOf(RoomNotFoundException.class);
    }

    private List<String> getKeyList(Long roomId, String startDate, String endDate) {
        List<String> keyList = new ArrayList<>();

        LocalDate targetDate = DateTimeUtil.toLocalDate(startDate);
        LocalDate maxDate = DateTimeUtil.toLocalDate(endDate);
        while (targetDate.isBefore(maxDate)) {
            keyList.add("roomId:" + roomId + ":" + targetDate);
            targetDate = targetDate.plusDays(1);
        }

        return keyList;
    }
}
