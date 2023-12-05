package com.fc.shimpyo_be.domain.room.unit.repository;

import com.fc.shimpyo_be.config.TestQuerydslConfig;
import com.fc.shimpyo_be.domain.product.entity.Category;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.room.dto.response.RoomWithProductResponseDto;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
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

import static org.assertj.core.api.Assertions.*;

@Slf4j
@Import(TestQuerydslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ProductRepository productRepository;

    private List<Long> roomIds;

    @BeforeEach
    void setUp() {
        Product product = productRepository.save(
            Product.builder()
                .name("호텔")
                .description("호텔 설명")
                .address("호텔 도로명 주소")
                .category(Category.TOURIST_HOTEL)
                .thumbnail("호텔 이미지 썸네일")
                .starAvg(3.5f)
                .build()
        );

        roomIds = new LinkedList<>();

        for (int i = 1; i <= 5; i++) {
            roomIds.add(
                roomRepository.save(
                    Room.builder()
                        .name("호텔 객실 " + i)
                        .description("객실 설명")
                        .product(product)
                        .standard(2)
                        .capacity(4)
                        .checkIn(LocalTime.of(13, 0))
                        .checkOut(LocalTime.of(12, 0))
                        .price(Integer.parseInt(String.format("1%d0000", i)))
                        .build()
                ).getId()
            );
        }
    }

    @DisplayName("findAllInRoomIdsResponseDto 테스트")
    @Test
    void findAllInRoomIdsResponseDto() {
        //given

        //when
        List<RoomWithProductResponseDto> result = roomRepository.findAllInRoomIdsResponseDto(roomIds);

        //then
        assertThat(result).hasSize(5);
        assertThat(result.get(0).checkIn()).isInstanceOf(String.class);
    }

    @AfterEach
    void tearDown() {
        roomRepository.deleteAll();
        productRepository.deleteAll();
    }
}
