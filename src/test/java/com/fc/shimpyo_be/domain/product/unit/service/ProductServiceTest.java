package com.fc.shimpyo_be.domain.product.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

import com.fc.shimpyo_be.domain.product.dto.request.SearchKeywordRequest;
import com.fc.shimpyo_be.domain.product.dto.response.PaginatedProductResponse;
import com.fc.shimpyo_be.domain.product.dto.response.ProductDetailsResponse;
import com.fc.shimpyo_be.domain.product.entity.Category;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.factory.ProductFactory;
import com.fc.shimpyo_be.domain.product.repository.ProductCustomRepositoryImpl;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.product.service.ProductService;
import com.fc.shimpyo_be.domain.product.util.ProductMapper;
import com.fc.shimpyo_be.domain.room.dto.response.RoomResponse;
import com.fc.shimpyo_be.domain.room.entity.Room;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductCustomRepositoryImpl productCustomRepository;

    @Spy
    @InjectMocks
    private ProductService productService;


    @Test
    void getProducts() {
        //given
        SearchKeywordRequest searchKeywordRequest = SearchKeywordRequest.builder()
            .productName("강릉 세인트 호텔").category(Category.MOTEL.getName()).address("강원도 강릉시 창해로 307")
            .build();

        List<Product> expectedProducts = ProductFactory.createTestProducts();
        Pageable pageable = Pageable.ofSize(10);
        Page<Product> productPage = new PageImpl<>(expectedProducts);
        given(productCustomRepository.findAllBySearchKeywordRequest(any(SearchKeywordRequest.class),
            any(Pageable.class))).willReturn(
            productPage);

        PaginatedProductResponse result = productService.getProducts(searchKeywordRequest,
            pageable);

        //then
        assertThat(result.productResponses()).usingRecursiveAssertion().isEqualTo(
            productPage.getContent().stream()
                .map(product -> ProductMapper.toProductResponse(product, false)).toList());
    }

    @Test
    void getProductDetails() {
        //given
        Product product = ProductFactory.createTestProduct();
        Room room = ProductFactory.createTestRoom(product, 0L);
        product.getRooms().add(room);
        given(productRepository.findById(product.getId())).willReturn(Optional.ofNullable(product));
        doReturn(1L).when(
                productService)
            .countAvailableForReservationUsingRoomCode(anyLong(), anyString(), anyString());

        //when
        ProductDetailsResponse result = productService.getProductDetails(product.getId(),
            "2023-11-27", "2023-11-28");
        //then
        for (int i = 0; i < result.rooms().size(); i++) {
            RoomResponse roomResponse = ProductMapper.toProductDetailsResponse(product, false)
                .rooms()
                .get(i);
            roomResponse.setRemaining(1L);
            assertThat(result.rooms().get(i)).usingRecursiveComparison()
                .isEqualTo(roomResponse);
        }

    }

}