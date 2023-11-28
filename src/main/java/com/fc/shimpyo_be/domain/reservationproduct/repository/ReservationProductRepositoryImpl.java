package com.fc.shimpyo_be.domain.reservationproduct.repository;

import com.fc.shimpyo_be.domain.reservationproduct.entity.ReservationProduct;
import com.fc.shimpyo_be.global.util.QueryDslUtil;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.ObjectUtils;

import java.util.LinkedList;
import java.util.List;

import static com.fc.shimpyo_be.domain.reservation.entity.QReservation.reservation;
import static com.fc.shimpyo_be.domain.reservationproduct.entity.QReservationProduct.reservationProduct;
import static com.fc.shimpyo_be.domain.room.entity.QRoom.room;

@RequiredArgsConstructor
public class ReservationProductRepositoryImpl implements ReservationProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ReservationProduct> findAllInReservationIds(List<Long> reservationIds, Pageable pageable) {

        List<ReservationProduct> content
            = jpaQueryFactory.selectFrom(reservationProduct)
            .join(reservationProduct.reservation, reservation).fetchJoin()
            .join(reservationProduct.room, room).fetchJoin()
            .where(reservation.id.in(reservationIds))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .orderBy(getAllOrderSpecifiers(pageable).toArray(OrderSpecifier[]::new))
            .fetch();

        JPAQuery<ReservationProduct> countQuery
            = jpaQueryFactory.selectFrom(reservationProduct)
            .join(reservationProduct.reservation, reservation)
            .join(reservationProduct.room, room)
            .where(reservation.id.in(reservationIds));

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }

    private List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier<?>> ORDERS = new LinkedList<>();

        if (!ObjectUtils.isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "id" -> {
                        OrderSpecifier<?> orderId = QueryDslUtil.getSortedColumn(direction, reservationProduct, "id");
                        ORDERS.add(orderId);
                    }
                    case "createdAt" -> {
                        OrderSpecifier<?> orderCreatedAt = QueryDslUtil.getSortedColumn(direction, reservation, "createdAt");
                        ORDERS.add(orderCreatedAt);
                    }
                }
            }
        }

        // 기본 default : 최신순
        if(ORDERS.isEmpty()) {
            ORDERS.add(QueryDslUtil.getSortedColumn(Order.DESC, reservationProduct, "id"));
        }

        return ORDERS;
    }
}
