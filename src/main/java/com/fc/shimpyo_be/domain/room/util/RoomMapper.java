package com.fc.shimpyo_be.domain.room.util;

import com.fc.shimpyo_be.domain.product.entity.Address;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.room.dto.response.RoomOptionResponse;
import com.fc.shimpyo_be.domain.room.dto.response.RoomResponse;
import com.fc.shimpyo_be.domain.room.dto.response.RoomWithProductResponseDto;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.entity.RoomOption;
import com.fc.shimpyo_be.global.util.DateTimeUtil;
import com.fc.shimpyo_be.global.util.PricePickerByDateUtil;

public class RoomMapper {

    public static RoomResponse toRoomResponse(Room room) {

        boolean isPeakTime = PricePickerByDateUtil.isPeakTime();
        boolean isWeekend = PricePickerByDateUtil.isWeekend();

        return RoomResponse.builder()
            .roomId(room.getId())
            .roomName(room.getName())
            // TODO 날짜에 따라 가격이 달라지므로 로직 수정이 필요함
            .price((PricePickerByDateUtil.getPrice(room)))
            .standard((long) (room.getStandard()))
            .capacity((long) room.getCapacity())
            .description(room.getDescription())
            .reserved(false)
            .checkIn(room.getCheckIn().toString())
            .checkOut(room.getCheckOut().toString())
            .roomOptionResponse(toRoomOptionResponse(room.getRoomOption()))
            .build();
    }

    public static RoomOptionResponse toRoomOptionResponse(RoomOption roomOption) {
        return RoomOptionResponse.builder()
            .airCondition(roomOption.isAirCondition())
            .pc(roomOption.isPc())
            .bath(roomOption.isBath())
            .bathFacility(roomOption.isBathFacility())
            .cooking(roomOption.isCooking())
            .tv(roomOption.isTv())
            .cable(roomOption.isCable())
            .hairDryer(roomOption.isHairDryer())
            .sofa(roomOption.isSofa())
            .table(roomOption.isDiningTable())
            .toiletries(roomOption.isToiletries())
            .homeTheater(roomOption.isHomeTheater())
            .internet(roomOption.isInternet())
            .refrigerator(roomOption.isRefrigerator())
            .build();
    }

    public static RoomWithProductResponseDto toRoomWithProductResponse(Room room) {
        Product product = room.getProduct();
        Address productAddress = product.getAddress();

        return RoomWithProductResponseDto.builder()
            .productId(product.getId())
            .productName(product.getName())
            .productThumbnail(product.getThumbnail())
            .productAddress(productAddress.getAddress())
            .productDetailAddress(productAddress.getDetailAddress())
            .roomId(room.getId())
            .roomName(room.getName())
            .standard(room.getStandard())
            .capacity(room.getCapacity())
            .checkIn(DateTimeUtil.toString(room.getCheckIn()))
            .checkOut(DateTimeUtil.toString(room.getCheckOut()))
            .price(PricePickerByDateUtil.getPrice(room))
            .build();
    }
}
