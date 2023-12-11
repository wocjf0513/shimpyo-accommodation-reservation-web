package com.fc.shimpyo_be.domain.product.unit.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fc.shimpyo_be.config.TestJpaConfig;
import com.fc.shimpyo_be.config.TestQuerydslConfig;
import com.fc.shimpyo_be.domain.product.dto.request.SearchKeywordRequest;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.factory.ProductFactory;
import com.fc.shimpyo_be.domain.product.repository.ProductCustomRepository;
import com.fc.shimpyo_be.domain.product.repository.ProductCustomRepositoryImpl;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@DataJpaTest
@ComponentScan(basePackages = {"com.fc.shimpyo_be.domain.product.repository"})
@Import({TestJpaConfig.class, TestQuerydslConfig.class})
public class ProductRepositoryTest {

    @Autowired
    ProductCustomRepositoryImpl productCustomRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    RoomRepository roomRepository;


    @Test
    public void getProducts() {
        //given
        Product product = ProductFactory.createTestProduct();
        Room room = ProductFactory.createTestRoom(product);

        productRepository.save(product);
        roomRepository.save(room);

        SearchKeywordRequest searchKeywordRequest = SearchKeywordRequest.builder()
            .productName("")
            .address("")
            .category("")
            .capacity(0L)
            .build();

        //when
        Page<Product> products = productCustomRepository.findAllBySearchKeywordRequest(
            searchKeywordRequest,
            Pageable.ofSize(10));

        //the
        assertEquals(1, products.getContent().size());
    }

}