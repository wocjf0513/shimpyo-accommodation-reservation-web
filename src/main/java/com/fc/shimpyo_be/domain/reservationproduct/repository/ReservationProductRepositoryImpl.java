package com.fc.shimpyo_be.domain.reservationproduct.repository;

import com.fc.shimpyo_be.domain.reservationproduct.entity.ReservationProduct;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

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
            .orderBy(getSort(pageable))
            .fetch();

        JPAQuery<ReservationProduct> countQuery
            = jpaQueryFactory.selectFrom(reservationProduct)
            .join(reservationProduct.reservation, reservation)
            .join(reservationProduct.room, room)
            .where(reservation.id.in(reservationIds));

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }

    private OrderSpecifier<?> getSort(Pageable pageable) {
        //서비스에서 보내준 Pageable 객체에 정렬조건 null 값 체크
        String property;
        if (!pageable.getSort().isEmpty()) {
            //정렬값이 들어 있으면 for 사용하여 값을 가져온다
            for (Sort.Order sortOrder : pageable.getSort()) {
                // 서비스에서 넣어준 DESC or ASC 를 가져온다.
                com.querydsl.core.types.Order direction = sortOrder.getDirection().isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC;
                // 서비스에서 넣어준 정렬 조건을 스위치 케이스 문을 활용하여 셋팅하여 준다.
                property = sortOrder.getProperty();
                switch (property) {
                    case "id" -> {
                        return new OrderSpecifier<>(direction, reservationProduct.id);
                    }
                    case "createdAt" -> {
                        return new OrderSpecifier<>(direction, reservation.createdAt);
                    }
                }
            }
        }

        return new OrderSpecifier<>(com.querydsl.core.types.Order.DESC, reservationProduct.id);
    }
}
