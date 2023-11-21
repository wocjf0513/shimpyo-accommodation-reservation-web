package com.fc.shimpyo_be.domain.product.util;

import com.fc.shimpyo_be.domain.product.dto.response.ProductDetailsResponse;
import com.fc.shimpyo_be.domain.product.dto.response.ProductResponse;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.room.dto.response.RoomResponse;
import com.fc.shimpyo_be.domain.room.util.RoomMapper;

public class ProductMapper {

    public static ProductResponse from(Product product) {
        return ProductResponse.builder().productId(product.getId()).productName(product.getName())
            .address(product.getAddress()).category(product.getCategory().toString())
            .image(ImageUrlParser.pareseThumbnail(product.getPhotoUrl())).star(product.getStarAvg())
            .build();
    }

    public static ProductDetailsResponse form(Product product) {
        return ProductDetailsResponse.builder()
            .productId(product.getId())
            .category(product.getCategory().toString())
            .address(product.getAddress())
            .productName(product.getName())
            .desc(product.getDescription())
            .starAvg(product.getStarAvg())
            .images(ImageUrlParser.parse(product.getPhotoUrl()))
            .rooms(product.getRooms().stream().map(RoomMapper::from).toList())
            .build();
    }


}
