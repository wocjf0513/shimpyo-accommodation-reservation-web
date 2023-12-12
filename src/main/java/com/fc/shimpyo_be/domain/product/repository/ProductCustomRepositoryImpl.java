package com.fc.shimpyo_be.domain.product.repository;


import static com.fc.shimpyo_be.domain.product.entity.QAddress.address1;
import static com.fc.shimpyo_be.domain.product.entity.QProduct.product;
import static com.fc.shimpyo_be.domain.room.entity.QRoom.room;

import com.fc.shimpyo_be.domain.product.dto.request.SearchKeywordRequest;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.global.util.QueryDslUtil;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;


@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory queryFactory;

    ProductCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.queryFactory = jpaQueryFactory;
    }

    public Page<Product> findAllBySearchKeywordRequest(SearchKeywordRequest searchKeywordRequest,
        Pageable pageable) {

        JPAQuery<Product> query = queryFactory
            .selectDistinct(product)
            .from(product)
            .leftJoin(product.rooms, room)
            .leftJoin(product.address, address1)
            .where(buildSearchConditions(searchKeywordRequest))
            .offset(pageable.getOffset())
            .orderBy(getAllOrderSpecifiers(pageable).toArray(OrderSpecifier[]::new))
            .limit(pageable.getPageSize());

        JPAQuery<Product> countQuery = queryFactory
            .selectDistinct(product)
            .from(product)
            .leftJoin(product.rooms, room)
            .leftJoin(product.address, address1)
            .where(buildSearchConditions(searchKeywordRequest));

        List<Product> content = query.fetch();
        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }

    private BooleanExpression buildSearchConditions(SearchKeywordRequest searchKeywordRequest) {
        List<BooleanExpression> expressions = new ArrayList<>();

        if (searchKeywordRequest.productName() != null) {
            expressions.add(product.name.containsIgnoreCase(searchKeywordRequest.productName()));
        }

        if (searchKeywordRequest.address() != null) {
            expressions.add(
                address1.detailAddress.containsIgnoreCase(searchKeywordRequest.address()));
        }

        if (searchKeywordRequest.category() != null && !searchKeywordRequest.category().isEmpty()) {
            expressions.add(product.category.in(searchKeywordRequest.category()));
        }

        if (searchKeywordRequest.capacity() != null) {
            expressions.add(room.capacity.goe(searchKeywordRequest.capacity()));
        }

        return expressions.stream().reduce(BooleanExpression::and).orElse(null);
    }


    private List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier<?>> ORDERS = new LinkedList<>();

        if (!ObjectUtils.isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "starAvg" -> {
                        OrderSpecifier<?> orderId = QueryDslUtil.getSortedColumn(direction, product,
                            "starAvg");
                        ORDERS.add(orderId);
                    }
                }
            }
        }

        if (ORDERS.isEmpty()) {
            ORDERS.add(QueryDslUtil.getSortedColumn(Order.DESC, product, "starAvg"));
        }

        return ORDERS;
    }

}
