package com.fc.shimpyo_be.domain.favorite.repository;

import static com.fc.shimpyo_be.domain.favorite.entity.QFavorite.favorite;
import static com.fc.shimpyo_be.domain.member.entity.QMember.member;
import static com.fc.shimpyo_be.domain.product.entity.QProduct.product;

import com.fc.shimpyo_be.domain.favorite.entity.Favorite;
import com.fc.shimpyo_be.global.util.QueryDslUtil;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.LinkedList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
public class FavoriteCustomRepositoryImpl implements FavoriteCustomRepository {

    private final JPAQueryFactory queryFactory;

    FavoriteCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.queryFactory = jpaQueryFactory;
    }

    @Override
    public Page<Favorite> findAllByMemberId(long memberId, Pageable pageable) {
        JPAQuery<Favorite> query = queryFactory
            .selectDistinct(favorite)
            .from(favorite)
            .leftJoin(favorite.member, member)
            .leftJoin(favorite.product, product)
            .where(member.id.eq(memberId))
            .offset(pageable.getOffset())
            .orderBy(getAllOrderSpecifiers(pageable).toArray(OrderSpecifier[]::new))
            .limit(pageable.getPageSize());
        JPAQuery<Favorite> countQuery = queryFactory
            .selectDistinct(favorite)
            .from(favorite)
            .leftJoin(favorite.member, member)
            .leftJoin(favorite.product, product)
            .where(member.id.eq(memberId));
        List<Favorite> content = query.fetch();
        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }

    private List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> ORDERS = new LinkedList<>();
        ORDERS.add(QueryDslUtil.getSortedColumn(Order.DESC, favorite, "id"));
        return ORDERS;
    }
}
