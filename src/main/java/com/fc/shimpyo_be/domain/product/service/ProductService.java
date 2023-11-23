package com.fc.shimpyo_be.domain.product.service;

import com.fc.shimpyo_be.domain.product.entity.Category;
import com.fc.shimpyo_be.domain.product.repository.model.ProductSpecification;
import com.fc.shimpyo_be.domain.product.dto.response.ProductDetailsResponse;
import com.fc.shimpyo_be.domain.product.dto.response.ProductResponse;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.exception.ProductNotFoundException;
import com.fc.shimpyo_be.domain.product.exception.RoomNotFoundException;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.product.util.ProductMapper;
import com.fc.shimpyo_be.domain.reservation.repository.ReservationRepository;
import com.fc.shimpyo_be.domain.room.dto.response.RoomResponse;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import com.fc.shimpyo_be.global.util.DateTimeUtil;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ReservationRepository reservationRepository;

    private final RoomRepository roomRepository;

    public List<ProductResponse> getAllProducts(final String productName, final String address,
        final String category, final Pageable pageable) {

        Specification<Product> spec = (root, query, criteriaBuilder) -> null;

        if (productName != null) {
            spec = spec.and(ProductSpecification.likeProductName(productName));
        }
        if (category != null) {
            if (category.contains(",")) {
                String[] categories = category.split(",");
                for (int i = 0; i < categories.length; i++) {
                    spec = spec.or(ProductSpecification.equalCategory(categories[i]));
                }
            }
            else{
                spec = spec.and(ProductSpecification.equalCategory(category));
            }
        }
        if (address != null) {
            spec = spec.and(ProductSpecification.likeAddress(address));
        }

        List<ProductResponse> allProducts = Optional.of(
                productRepository.findAll(spec, pageable)).orElseThrow().getContent()
            .stream().map(ProductMapper::toProductResponse).toList();

        if (allProducts.isEmpty()) {
            throw new ProductNotFoundException();
        }
        return allProducts;
    }

    public ProductDetailsResponse getProductDetails(final Long productId, final String startDate,
        final String endDate) {
        Product product = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);
        ProductDetailsResponse productDetailsResponse = ProductMapper.toProductDetailsResponse(product);
//        productDetailsResponse.rooms().stream().filter(
//                roomResponse -> !isAvailableForReservation(roomResponse.getRoomId(), startDate, endDate))
//            .forEach(
//                RoomResponse::setReserved);
        return productDetailsResponse;
    }


//    public boolean isAvailableForReservation(final Long roomId, final String startDate,
//        final String endDate) {
//        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
//        Long countReservedRooms = reservationRepository.countReservedRooms(roomId,
//            DateTimeUtil.toLocalDateTime(startDate),
//            DateTimeUtil.toLocalDateTime(endDate).minusDays(1));
//
//        if (countReservedRooms == null || countReservedRooms == 0L) {
//            return true;
//        }
//        return false;
//    }


}
