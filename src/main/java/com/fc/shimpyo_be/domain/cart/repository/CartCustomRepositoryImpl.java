package com.fc.shimpyo_be.domain.cart.repository;

import static com.fc.shimpyo_be.domain.cart.entity.QCart.cart;

import com.fc.shimpyo_be.domain.cart.dto.request.CartCreateRequest;
import com.fc.shimpyo_be.global.util.DateTimeUtil;
import com.querydsl.core.QueryException;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CartCustomRepositoryImpl implements CartCustomRepository {

    private final JPAQueryFactory queryFactory;

    CartCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.queryFactory = jpaQueryFactory;
    }

    @Override
    public Long countByRoomCodeAndMemberIdContainsDate(CartCreateRequest cartCreateRequest,
        Long memberId) {

        LocalDate startDate = DateTimeUtil.toLocalDate(cartCreateRequest.startDate());
        LocalDate endDate = DateTimeUtil.toLocalDate(cartCreateRequest.endDate());

        return queryFactory
            .selectFrom(cart)
            .leftJoin(cart.member)
            .where(buildSearchConditions(cartCreateRequest.roomCode(), memberId,
                startDate, endDate)).fetchCount();
    }

    private BooleanExpression buildSearchConditions(Long roomCode, Long memberId,
        LocalDate startDate, LocalDate endDate) {
        List<BooleanExpression> expressions = new ArrayList<>();

        if (roomCode == null || memberId == null || startDate == null || endDate == null) {
            throw new QueryException("잘못된 쿼리 입니다.");
        }

        expressions.add(cart.member.id.eq(memberId).and(cart.roomCode.eq(roomCode))
            .and(cart.startDate.before(endDate).and(cart.endDate.after(startDate))));

        return expressions.stream().reduce(BooleanExpression::and).orElse(null);
    }
}
