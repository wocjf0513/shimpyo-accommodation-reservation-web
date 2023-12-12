package com.fc.shimpyo_be.domain.product.repository;


import com.fc.shimpyo_be.domain.product.dto.request.SearchKeywordRequest;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.entity.QAddress;
import com.fc.shimpyo_be.domain.product.entity.QProduct;
import com.fc.shimpyo_be.domain.room.entity.QRoom;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;


@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory queryFactory;

    ProductCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.queryFactory = jpaQueryFactory;
    }

    public Page<Product> findAllBySearchKeywordRequest(SearchKeywordRequest searchKeywordRequest,
        Pageable pageable) {

        QProduct product = QProduct.product;
        QRoom room = QRoom.room;
        QAddress address = QAddress.address1;

        JPAQuery<Product> query = queryFactory
            .selectDistinct(product)
            .from(product)
            .leftJoin(product.rooms, room).fetchJoin()
            .leftJoin(product.address, address).fetchJoin()
            .where(product.name.likeIgnoreCase("%" + searchKeywordRequest.productName() + "%"),
                address.detailAddress.likeIgnoreCase("%" + searchKeywordRequest.address() + "%"),
                product.category.in(searchKeywordRequest.category()),
                room.capacity.goe(searchKeywordRequest.capacity()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        List<Product> content = query.fetch();
        return PageableExecutionUtils.getPage(content, pageable, query::fetchCount);
    }
}
