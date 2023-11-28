package com.fc.shimpyo_be.domain.product.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.fc.shimpyo_be.domain.product.dto.request.SearchKeywordRequest;
import com.fc.shimpyo_be.domain.product.entity.Category;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.product.repository.model.ProductSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;
    @InjectMocks
    ProductService productService;

    @Test
    void getProducts() {
        //given
        SearchKeywordRequest searchKeywordRequest = SearchKeywordRequest.builder()
            .productName("강릉 세인트 호텔").category(Category.MOTEL.getName()).address("강원도 강릉시 창해로 307")
            .build();

        Specification<Product> spec = (root, query, criteriaBuilder) -> null;

        if (searchKeywordRequest.productName() != null) {
            spec = spec.and(
                ProductSpecification.likeProductName(searchKeywordRequest.productName()));
        }
        if (searchKeywordRequest.category() != null) {
            if (searchKeywordRequest.category().contains(",")) {
                String[] categories = searchKeywordRequest.category().split(",");
                for (int i = 0; i < categories.length; i++) {
                    spec = spec.or(ProductSpecification.equalCategory(categories[i]));
                }
            } else {
                spec = spec.and(
                    ProductSpecification.equalCategory(searchKeywordRequest.category()));
            }
        }
        if (searchKeywordRequest.address() != null) {
            spec = spec.and(ProductSpecification.likeAddress(searchKeywordRequest.address()));
        }

        Pageable pageable = Pageable.ofSize(10);


        //when


        //then

    }

    @Test
    void getProductDetails() {
        //given

        //when

        //then
    }

    @Test
    void isAvailableForReservation() {
        //given

        //when

        //then
    }
}