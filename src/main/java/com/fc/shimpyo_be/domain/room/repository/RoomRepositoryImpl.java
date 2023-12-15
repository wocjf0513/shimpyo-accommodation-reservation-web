package com.fc.shimpyo_be.domain.room.repository;

import com.fc.shimpyo_be.domain.room.entity.Room;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.fc.shimpyo_be.domain.product.entity.QProduct.product;
import static com.fc.shimpyo_be.domain.room.entity.QRoom.room;
import static com.fc.shimpyo_be.domain.room.entity.QRoomPrice.roomPrice;

@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Room> findAllInIdsWithProductAndPrice(List<Long> roomIds) {
        return jpaQueryFactory.selectFrom(room)
            .join(room.product, product).fetchJoin()
            .join(room.price, roomPrice).fetchJoin()
            .where(room.id.in(roomIds))
            .fetch();
    }
}
