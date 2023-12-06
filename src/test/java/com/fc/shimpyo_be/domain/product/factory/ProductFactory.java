package com.fc.shimpyo_be.domain.product.factory;

import com.fc.shimpyo_be.domain.product.entity.Address;
import com.fc.shimpyo_be.domain.product.entity.Category;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.entity.ProductImage;
import com.fc.shimpyo_be.domain.product.entity.ProductOption;
import com.fc.shimpyo_be.domain.product.model.Area;
import com.fc.shimpyo_be.domain.product.model.RandomProductInfo;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.entity.RoomOption;
import com.fc.shimpyo_be.domain.room.entity.RoomPrice;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ProductFactory {


    public static Product createTestProduct() {
        String area = Area.values()[ThreadLocalRandom.current()
            .nextInt(Area.values().length)].toString();
        return Product.builder().name(area + " 숙박").address(Address.builder()
                .address("서울시" + area)
                .detailAddress("상세주소")
                .mapX(1.0)
                .mapY(1.0)
                .build())
            .thumbnail(RandomProductInfo.genRandomImage()).category(
                Category.values()[ThreadLocalRandom.current().nextInt(Category.values().length)])
            .starAvg(ThreadLocalRandom.current().nextFloat(5))
            .description(RandomProductInfo.genRandomDescription())
            .rooms(new ArrayList<>())
            .productOption(ProductOption.builder()
                .cooking(false)
                .parking(false)
                .pickup(false)
                .foodPlace("")
                .infoCenter("000-0000-0000")
                .build())
            .build();
    }

    public static ProductImage createTestProductImage(Product product) {
        return ProductImage.builder().product(product).photoUrl(RandomProductInfo.genRandomImage())
            .build();
    }

    public static Room createTestRoom(Product product) {

        int stadard = ThreadLocalRandom.current().nextInt(10);
        int fee = ThreadLocalRandom.current().nextInt(100000);

        return Room.builder().price(RoomPrice.builder()
                .offWeekDaysMinFee(fee)
                .offWeekendMinFee(fee + 10000)
                .peakWeekDaysMinFee(fee + 50000)
                .peakWeekendMinFee(fee + 60000)
                .build())
            .description(RandomProductInfo.genRandomDescription()).product(product)
            .checkIn(LocalTime.of(11, 0, 0)).checkOut(LocalTime.of(15, 0, 0))
            .name(product.getCategory().getName() + " 방").standard(stadard)
            .capacity(stadard + ThreadLocalRandom.current().nextInt(10))
            .roomImages(new ArrayList<>())
            .roomOption(RoomOption.builder()
                .bathFacility(false)
                .bath(false)
                .homeTheater(false)
                .airCondition(false)
                .tv(false)
                .pc(false)
                .cable(false)
                .internet(false)
                .refrigerator(false)
                .toiletries(false)
                .sofa(false)
                .cooking(false)
                .table(false)
                .hairDryer(false)
                .build())
            .build();

    }

    public static List<Product> createTestProducts() {
        List<Product> productList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            productList.add(createTestProduct());
        }
        return productList;
    }


}
