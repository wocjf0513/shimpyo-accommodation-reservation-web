package com.fc.shimpyo_be.domain.room.repository;

import com.fc.shimpyo_be.domain.room.dto.response.RoomWithProductResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.TimePath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.List;

import static com.fc.shimpyo_be.domain.product.entity.QProduct.product;
import static com.fc.shimpyo_be.domain.room.entity.QRoom.room;

@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RoomWithProductResponseDto> findAllInRoomIdsResponseDto(List<Long> roomIds) {
        return jpaQueryFactory.select(
                Projections.constructor(
                    RoomWithProductResponseDto.class,
                    product.id.as("productId"),
                    product.name.as("productName"),
                    product.thumbnail.as("productThumbnail"),
                    product.address.as("productAddress"),
                    room.id.as("roomId"),
                    room.name.as("roomName"),
                    room.standard,
                    room.capacity,
                    convertTimeToString(room.checkIn).as("checkIn"),
                    convertTimeToString(room.checkOut).as("checkOut"),
                    room.price
                )
            )
            .from(room)
            .join(room.product, product)
            .where(room.id.in(roomIds))
            .fetch();
    }

    private StringExpression convertTimeToString(TimePath<LocalTime> time) {
        return Expressions.stringTemplate("TO_CHAR({0}, 'HH24:MI')", time);
    }
}
