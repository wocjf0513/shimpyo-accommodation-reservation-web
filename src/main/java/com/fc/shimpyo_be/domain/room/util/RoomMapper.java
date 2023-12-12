package com.fc.shimpyo_be.domain.room.util;

import com.fc.shimpyo_be.domain.room.dto.response.RoomOptionResponse;
import com.fc.shimpyo_be.domain.room.dto.response.RoomResponse;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.entity.RoomImage;
import com.fc.shimpyo_be.domain.room.entity.RoomOption;
import com.fc.shimpyo_be.global.util.PricePickerByDateUtil;

public class RoomMapper {

    public static RoomResponse toRoomResponse(Room room) {

        long price = PricePickerByDateUtil.getPrice(room);
        price = price == 0 ? 100000 : price;

        return RoomResponse.builder()
            .roomCode(room.getCode())
            .roomName(room.getName())
            .price(price)
            .standard((long) (room.getStandard()))
            .capacity((long) room.getCapacity())
            .description(room.getDescription())
            .checkIn(room.getCheckIn().toString())
            .checkOut(room.getCheckOut().toString())
            .roomOptionResponse(toRoomOptionResponse(room.getRoomOption()))
            .roomImages(room.getRoomImages().stream().map(RoomImage::getPhotoUrl).toList())
            .remaining(0L)
            .build();
    }

    private static RoomOptionResponse toRoomOptionResponse(RoomOption roomOption) {
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
}
