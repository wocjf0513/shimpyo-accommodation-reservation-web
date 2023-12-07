package com.fc.shimpyo_be.domain.product.util;

import com.fc.shimpyo_be.domain.product.dto.response.ProductAmenityResponse;
import com.fc.shimpyo_be.domain.product.dto.response.ProductDetailsResponse;
import com.fc.shimpyo_be.domain.product.dto.response.ProductOptionResponse;
import com.fc.shimpyo_be.domain.product.dto.response.ProductResponse;
import com.fc.shimpyo_be.domain.product.entity.Amenity;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.entity.ProductImage;
import com.fc.shimpyo_be.domain.product.entity.ProductOption;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.entity.RoomPrice;
import com.fc.shimpyo_be.domain.room.util.RoomMapper;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class ProductMapper {

    public static ProductResponse toProductResponse(Product product) {

        LocalDate today = LocalDate.now();
        boolean isPeakMonth = true;
        boolean isWeekend = false;

        isPeakMonth = switch (today.getMonth()) {
            //성수기
            case MARCH, APRIL, MAY, JUNE, SEPTEMBER -> false;
            default -> isPeakMonth;
        };

        isWeekend = switch (today.getDayOfWeek().getValue()) {
            case 6, 7 -> true;
            default -> isWeekend;
        };

        boolean finalIsPeakMonth = isPeakMonth;
        boolean finalIsWeekend = isWeekend;

        return ProductResponse.builder().productId(product.getId()).productName(product.getName())
            .address(
                product.getAddress().getAddress() + " " + product.getAddress().getDetailAddress())
            .category(product.getCategory().getName())
            .image(product.getThumbnail())
            .starAvg(product.getStarAvg())
            .price(product.getRooms().isEmpty()
                ? 0 : Long.valueOf(
                // TODO 해당 숙소 객실 가격 중 가장 낮은 가격을 반환해야 하지만, 날짜에 따라 가격이 달라지므로 로직 수정이 필요함
                product.getRooms().stream().map(Room::getPrice).map(roomPrice -> {
                        if (finalIsPeakMonth && finalIsWeekend) {
                            return roomPrice.getPeakWeekendMinFee();
                        } else if (finalIsPeakMonth && !finalIsWeekend) {
                            return roomPrice.getPeakWeekDaysMinFee();
                        } else if (!finalIsPeakMonth && finalIsWeekend) {
                            return roomPrice.getOffWeekendMinFee();
                        } else {
                            return roomPrice.getOffWeekDaysMinFee();
                        }
                    })
                    .min((o1, o2) -> o1 - o2)
                    .orElseThrow()))
            .capacity(product.getRooms().isEmpty()
                ? 0 : Long.valueOf(
                product.getRooms().stream().map(Room::getCapacity).min((o1, o2) -> o2 - o1)
                    .orElseThrow()))
            .favorites(false)
            .build();
    }

    public static ProductDetailsResponse toProductDetailsResponse(Product product) {

        List<String> images = new ArrayList<>();
        images.add(product.getThumbnail());
        images.addAll(product.getPhotoUrls().stream().map(ProductImage::getPhotoUrl).toList());

        return ProductDetailsResponse.builder()
            .productId(product.getId())
            .category(product.getCategory().getName())
            .address(
                product.getAddress().getAddress() + " " + product.getAddress().getDetailAddress())
            .productName(product.getName())
            .description(product.getDescription())
            .productAmenityResponse(toProductAmenityResponse(product.getAmenity()))
            .starAvg(product.getStarAvg())
            .productOptionResponse(toProductOptionResponse(product.getProductOption()))
            .favorites(false)
            .images(images)
            .rooms(product.getRooms().stream().map(RoomMapper::from).toList())
            .build();
    }

    public static ProductAmenityResponse toProductAmenityResponse(Amenity amenity) {
        return ProductAmenityResponse.builder()
            .barbecue(amenity.isBarbecue())
            .bicycle(amenity.isBicycle())
            .beauty(amenity.isBeauty())
            .beverage(amenity.isBeverage())
            .sauna(amenity.isSauna())
            .fitness(amenity.isFitness())
            .karaoke(amenity.isKaraoke())
            .publicBath(amenity.isPublicBath())
            .publicPc(amenity.isPublicPc())
            .seminar(amenity.isSeminar())
            .sports(amenity.isSports())
            .campfire(amenity.isCampfire())
            .build();
    }

    public static ProductOptionResponse toProductOptionResponse(ProductOption productOption) {
        return ProductOptionResponse.builder()
            .pickup(productOption.isPickup())
            .parking(productOption.isParking())
            .cooking(productOption.isCooking())
            .infoCenter(productOption.getInfoCenter())
            .foodPlace(productOption.getFoodPlace())
            .build();
    }

}
