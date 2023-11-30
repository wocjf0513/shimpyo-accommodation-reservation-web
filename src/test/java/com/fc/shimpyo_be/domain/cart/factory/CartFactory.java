package com.fc.shimpyo_be.domain.cart.factory;

import com.fc.shimpyo_be.domain.cart.dto.response.CartResponse;
import com.fc.shimpyo_be.domain.cart.entity.Cart;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.room.entity.Room;
import java.time.LocalDate;

public class CartFactory {

    public static Cart createCartTest(Room room, Member member) {
        return Cart.builder().room(room).member(member).price(
                    100000L
                ).startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(2)).build();
    }


}
