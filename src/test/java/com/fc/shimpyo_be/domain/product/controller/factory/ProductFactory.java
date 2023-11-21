package com.fc.shimpyo_be.domain.product.controller.factory;

import com.fc.shimpyo_be.domain.product.entity.Category;
import com.fc.shimpyo_be.domain.product.entity.Product;
import java.util.concurrent.ThreadLocalRandom;

public class ProductFactory {

    public static Product createTestProduct() {
        return Product.builder()
            .name("숙박"+ ThreadLocalRandom.current().nextInt(1000))
            .address("서울시 강남구 38-"+ThreadLocalRandom.current().nextInt(1000))
            .photoUrl("wjcojaodfjoadsfj,djaofjofjods,fadjofjodsafj")
            .category(Category.HOTEL)
            .description("좋아요")
            .build();
    }

}
