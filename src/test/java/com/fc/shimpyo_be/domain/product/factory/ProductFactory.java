package com.fc.shimpyo_be.domain.product.factory;

import java.util.*;
import com.fc.shimpyo_be.domain.product.entity.Category;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.entity.ProductImage;
import com.fc.shimpyo_be.domain.product.model.Area;
import com.fc.shimpyo_be.domain.product.model.RandomProductInfo;
import com.fc.shimpyo_be.domain.room.entity.Room;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ProductFactory {


    public static Product createTestProduct() {
        String area = Area.values()[ThreadLocalRandom.current()
            .nextInt(Area.values().length)].toString();
        return Product.builder().name(area + " 숙박").address("서울시" + area)
            .thumbnail(RandomProductInfo.genRandomImage()).category(
                Category.values()[ThreadLocalRandom.current().nextInt(Category.values().length)])
            .starAvg(ThreadLocalRandom.current().nextFloat(5))
            .description(RandomProductInfo.genRandomDescription()).build();
    }

    public static ProductImage createTestProductImage(Product product) {
        return ProductImage.builder().product(product).photoUrl(RandomProductInfo.genRandomImage())
            .build();
    }

    public static Room createTestRoom(Product product) {

        int stadard = ThreadLocalRandom.current().nextInt(10);

        return Room.builder().price(ThreadLocalRandom.current().nextInt(100000))
            .description(RandomProductInfo.genRandomDescription()).product(product)
            .checkIn(LocalTime.of(11, 0, 0)).checkOut(LocalTime.of(15, 0, 0))
            .name(product.getCategory().getName() + " 방").standard(stadard)
            .capacity(stadard + ThreadLocalRandom.current().nextInt(10)).build();

    }

    public static List<Product> createTestProducts() {
        List<Product> productList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            productList.add(createTestProduct());
        }
        return productList;
    }


}
