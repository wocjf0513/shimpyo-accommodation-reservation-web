package com.fc.shimpyo_be.domain.room.unit.repository;

import com.fc.shimpyo_be.config.DatabaseCleanUp;
import com.fc.shimpyo_be.config.TestDBCleanerConfig;
import com.fc.shimpyo_be.config.TestQuerydslConfig;
import com.fc.shimpyo_be.domain.product.entity.*;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.entity.RoomOption;
import com.fc.shimpyo_be.domain.room.entity.RoomPrice;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Import({TestQuerydslConfig.class, TestDBCleanerConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    private final String[] tableNameArray = {
        "product", "room", "product_option", "address", "room_option", "amenity", "room_price"
    };

    private List<Long> roomIds;

    @BeforeEach
    void setUp() {
        databaseCleanUp.cleanUp(tableNameArray);

        Product product = productRepository.save(
            Product.builder()
                .name("호텔")
                .thumbnail("호텔 썸네일 url")
                .description("호텔 설명")
                .starAvg(4.2f)
                .category(Category.TOURIST_HOTEL)
                .address(
                    Address.builder()
                        .address("호텔 주소")
                        .detailAddress("호텔 상세 주소")
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
                        .infoCenter("1500-0000")
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

        roomIds = new LinkedList<>();

        for (int i = 1; i <= 5; i++) {
            String roomName = "호텔 객실" + i;
            roomIds.add(
                roomRepository.save(
                    Room.builder()
                        .product(product)
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
                ).getId()
            );
        }
    }

    @DisplayName("findAllInIdsWithProductAndPrice 테스트")
    @Test
    void findAllInIdsWithProductAndPrice() {
        //given

        //when
        List<Room> result = roomRepository.findAllInIdsWithProductAndPrice(roomIds);

        //then
        assertThat(result).hasSize(5);
        assertThat(result.get(0).getProduct().getId()).isEqualTo(1);
        assertThat(result.get(0).getPrice()).isNotNull();
    }
}
