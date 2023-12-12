package com.fc.shimpyo_be.domain.room.unit.service;

import com.fc.shimpyo_be.domain.product.entity.*;
import com.fc.shimpyo_be.domain.room.dto.response.RoomWithProductResponseDto;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.entity.RoomOption;
import com.fc.shimpyo_be.domain.room.entity.RoomPrice;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import com.fc.shimpyo_be.domain.room.service.RoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    @DisplayName("객실 식별자 리스트에 해당하는 객실과 숙소 정보를 리스트로 반환한다.")
    @Test
    void getRoomsWithProductInfo_test() {
        //given
        List<Long> roomIds = List.of(1L, 3L, 4L);

        Product product = Product.builder()
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
            .build();

        List<Room> rooms = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            String roomName = "호텔 객실" + i;
            rooms.add(
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
            );
        }

        given(roomRepository.findAllInIdsWithProductAndPrice(roomIds)).willReturn(rooms);

        //when
        List<RoomWithProductResponseDto> result = roomService.getRoomsWithProductInfo(roomIds);

        //then
        assertThat(result).hasSize(3);

        verify(roomRepository, times(1)).findAllInIdsWithProductAndPrice(roomIds);
    }
}
