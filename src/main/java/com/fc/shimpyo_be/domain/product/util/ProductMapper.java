package com.fc.shimpyo_be.domain.product.util;

import com.fc.shimpyo_be.domain.product.dto.response.ProductAddressResponse;
import com.fc.shimpyo_be.domain.product.dto.response.ProductAmenityResponse;
import com.fc.shimpyo_be.domain.product.dto.response.ProductDetailsResponse;
import com.fc.shimpyo_be.domain.product.dto.response.ProductOptionResponse;
import com.fc.shimpyo_be.domain.product.dto.response.ProductResponse;
import com.fc.shimpyo_be.domain.product.entity.Address;
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

        List<Room> rooms = product.getRooms();
        long price = rooms.isEmpty() ? 0 : rooms.stream().map(PricePickerByDateUtil::getPrice)
            .min((o1, o2) -> Math.toIntExact(
                o1 - o2)).orElseThrow();
        price = price == 0 ? 100000 : price;

        return ProductResponse.builder().productId(product.getId()).productName(product.getName())
            .address(
                product.getAddress().getAddress() + " " + product.getAddress().getDetailAddress())
            .category(product.getCategory().getName())
            .image(product.getThumbnail())
            .starAvg(product.getStarAvg())
            .price(price)
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

        if (product.getPhotoUrls() != null) {
            images.addAll(product.getPhotoUrls().stream().map(ProductImage::getPhotoUrl).toList());
        }

        return ProductDetailsResponse.builder()
            .productId(product.getId())
            .category(product.getCategory().getName())
            .address(toProductAddressResponse(product.getAddress()))
            .productName(product.getName())
            .description(product.getDescription())
            .productAmenityResponse(toProductAmenityResponse(product.getAmenity()))
            .starAvg(product.getStarAvg())
            .productOptionResponse(toProductOptionResponse(product.getProductOption()))
            .favorites(false)
            .images(images)
            .rooms(product.getRooms().stream().map(RoomMapper::toRoomResponse).distinct().toList())
            .build();
    }

    private static ProductAddressResponse toProductAddressResponse(Address address) {
        return ProductAddressResponse.builder()
            .address(address.getAddress())
            .detailAddress(address.getDetailAddress())
            .mapX(address.getMapX())
            .mapY(address.getMapY())
            .build();
    }

    private static ProductAmenityResponse toProductAmenityResponse(Amenity amenity) {
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

    private static ProductOptionResponse toProductOptionResponse(ProductOption productOption) {
        return ProductOptionResponse.builder()
            .pickup(productOption.isPickup())
            .parking(productOption.isParking())
            .cooking(productOption.isCooking())
            .infoCenter(productOption.getInfoCenter())
            .foodPlace(productOption.getFoodPlace())
            .build();
    }

}
