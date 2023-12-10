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
import com.fc.shimpyo_be.domain.room.util.RoomMapper;
import com.fc.shimpyo_be.global.util.PricePickerByDateUtil;
import java.util.ArrayList;
import java.util.List;

public class ProductMapper {

    public static ProductResponse toProductResponse(Product product) {

        return ProductResponse.builder().productId(product.getId()).productName(product.getName())
            .address(
                product.getAddress().getAddress() + " " + product.getAddress().getDetailAddress())
            .category(product.getCategory().getName())
            .image(product.getThumbnail())
            .starAvg(product.getStarAvg())
            .price(product.getRooms().isEmpty()
                ? 0 :
                product.getRooms().stream().map(PricePickerByDateUtil::getPrice)
                    .min((o1, o2) -> Math.toIntExact(o1 - o2))
                    .orElseThrow())
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

        if(product.getPhotoUrls() != null) {
            images.addAll(product.getPhotoUrls().stream().map(ProductImage::getPhotoUrl).toList());
        }

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
            .rooms(product.getRooms().stream().map(RoomMapper::toRoomResponse).toList())
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
