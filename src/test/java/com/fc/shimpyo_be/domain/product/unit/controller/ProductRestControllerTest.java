package com.fc.shimpyo_be.domain.product.unit.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import com.fc.shimpyo_be.domain.product.controller.ProductRestController;
import com.fc.shimpyo_be.domain.product.dto.request.SearchKeywordRequest;
import com.fc.shimpyo_be.domain.product.dto.response.ProductDetailsResponse;
import com.fc.shimpyo_be.domain.product.dto.response.ProductResponse;
import com.fc.shimpyo_be.domain.product.entity.Category;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.exception.RoomNotReserveException;
import com.fc.shimpyo_be.domain.product.factory.ProductFactory;
import com.fc.shimpyo_be.domain.product.service.ProductService;
import com.fc.shimpyo_be.domain.product.util.ProductMapper;
import com.fc.shimpyo_be.global.common.ResponseDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ProductRestControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductRestController productRestController;

    @Test
    void getAllProducts() {
        //given
        List<ProductResponse> productResponses = new ArrayList<>();
        productResponses.add(ProductMapper.toProductResponse(ProductFactory.createTestProduct()));
        SearchKeywordRequest searchKeywordRequest = SearchKeywordRequest.builder()
            .category(Category.MOTEL.getName()).build();
        Pageable pageable = Pageable.ofSize(10);
        doReturn(productResponses).when(productService).getProducts(searchKeywordRequest, pageable);
        ResponseEntity<ResponseDto<List<ProductResponse>>> result = productRestController.getProducts(
            searchKeywordRequest.productName(), searchKeywordRequest.address(),
            searchKeywordRequest.category(), pageable);
        //then
        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertThat(result.getBody().getData()).usingRecursiveComparison()
            .isEqualTo(productResponses);
    }

    @Test
    void getProductDetails() {
        //given
        Product product = ProductFactory.createTestProduct();
        ProductDetailsResponse expectedResult = ProductMapper.toProductDetailsResponse(product);
        doReturn(expectedResult).when(productService)
            .getProductDetails(1L, "2023-11-27", "2023-11-28");
        //when
        ResponseEntity<ResponseDto<ProductDetailsResponse>> result = productRestController.getProductDetails(
            1L, "2023-11-27", "2023-11-28");
        //then
        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertThat(result.getBody().getData()).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    @Test
    void isAvailableForReservation() {
        //given
        doReturn(false).when(productService)
            .isAvailableForReservation(1L, "2023-11-27", "2023-11-28");
        //when & then
        assertThrows(RoomNotReserveException.class,
            () -> productRestController.isAvailableForReservation(1L, "2023-11-27", "2023-11-28"));


    }
}