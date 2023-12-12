package com.fc.shimpyo_be.domain.cart.util;

import com.fc.shimpyo_be.domain.cart.dto.request.CartCreateRequest;
import com.fc.shimpyo_be.domain.cart.dto.response.CartDeleteResponse;
import com.fc.shimpyo_be.domain.cart.dto.response.CartResponse;
import com.fc.shimpyo_be.domain.cart.entity.Cart;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.global.util.DateTimeUtil;

public class CartMapper {

    public static CartResponse toCartResponse(Cart cart, Room room) {
        Product product = room.getProduct();

        return CartResponse.builder().cartId(cart.getId()).productId(product.getId())
            .productName(product.getName()).image(product.getThumbnail()).roomCode(room.getCode())
            .roomName(room.getName()).price(cart.getPrice()).description(room.getDescription())
            .standard((long) room.getStandard()).capacity((long) room.getCapacity())
            .startDate(DateTimeUtil.toString(cart.getStartDate()))
            .endDate(DateTimeUtil.toString(cart.getEndDate())).checkIn(room.getCheckIn().toString())
            .checkOut(room.getCheckOut().toString()).build();
    }

    public static CartDeleteResponse toCartDeleteResponse(Cart cart) {
        return CartDeleteResponse.builder().cartId(cart.getId()).roomCode(cart.getRoomCode())
            .startDate(DateTimeUtil.toString(cart.getStartDate()))
            .endDate(DateTimeUtil.toString(cart.getEndDate())).build();
    }

    public static Cart toCart(CartCreateRequest cartCreateRequest, Member member) {
        return Cart.builder()
            .roomCode(cartCreateRequest.roomCode())
            .member(member)
            .price(cartCreateRequest.price())
            .startDate(DateTimeUtil.toLocalDate(cartCreateRequest.startDate()))
            .endDate(DateTimeUtil.toLocalDate(cartCreateRequest.endDate()))
            .build();
    }
}
