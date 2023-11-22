package com.fc.shimpyo_be.domain.product.service;

import com.fc.shimpyo_be.domain.product.dto.response.ProductDetailsResponse;
import com.fc.shimpyo_be.domain.product.dto.response.ProductResponse;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.exception.ProductNotFoundException;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.product.util.ProductMapper;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final RoomRepository roomRepository;

    //예약 상품 목록을 조회해야 될듯.

    public List<ProductResponse> getAllProducts(final String keyword, final Pageable pageable) {
        List<ProductResponse> allProducts = Optional.of(
                productRepository.findAllContainingKeyword(keyword, pageable)).orElseThrow()
            .stream().map(ProductMapper::toProductResponse).toList();

        if (allProducts.isEmpty()) {
            throw new ProductNotFoundException();
        }
        return allProducts;
    }

//    public ProductDetailsResponse getProductDetails(final Long productId, final String startDate,
//        final String endDate) {
//        Product product = productRepository.findById(productId)
//            .orElseThrow(ProductNotFoundException::new);
//        ProductDetailsResponse productDetailsResponse = ProductMapper.toProductDetailsResponse(
//            product);
//        productDetailsResponse.rooms()
//            .removeIf(room -> isAvailableForReservation(room.roomId(), startDate, endDate));
//        return productDetailsResponse;
//    }


//    public boolean isAvailableForReservation(final Long roomId, final String startDate,
//        final String endDate) {
//        //여행 기간에 포함된 주문들을 다 보면서 날짜 하나라도 수량을 넘지는 않는지 확인
//
//    }


}
