package com.fc.shimpyo_be.domain.product.service;

import com.fc.shimpyo_be.domain.product.dto.request.SearchKeywordRequest;
import com.fc.shimpyo_be.domain.product.dto.response.PaginatedProductResponse;
import com.fc.shimpyo_be.domain.product.dto.response.ProductDetailsResponse;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.exception.ProductNotFoundException;
import com.fc.shimpyo_be.domain.product.repository.ProductCustomRepositoryImpl;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.product.util.ProductMapper;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import com.fc.shimpyo_be.global.util.DateTimeUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductCustomRepositoryImpl productCustomRepository;

    private final RoomRepository roomRepository;

    private final RedisTemplate<String, Object> restTemplate;

    public PaginatedProductResponse getProducts(final SearchKeywordRequest searchKeywordRequest,
        final Pageable pageable) {

        Page<Product> products = Optional.of(
                productCustomRepository.findAllBySearchKeywordRequest(searchKeywordRequest, pageable))
            .orElseThrow();

        return PaginatedProductResponse.builder()
            .productResponses(
                products.getContent().stream().map(ProductMapper::toProductResponse).toList())
            .pageCount(products.getTotalPages())
            .build();
    }

    public ProductDetailsResponse getProductDetails(final Long productId, final String startDate,
        final String endDate) {
        Product product = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);
        ProductDetailsResponse productDetailsResponse = ProductMapper.toProductDetailsResponse(
            product);
        productDetailsResponse.rooms().forEach(
            roomResponse -> roomResponse.setRemaining(
                countAvailableForReservationUsingRoomCode(roomResponse.getRoomCode(), startDate,
                    endDate)));
        return productDetailsResponse;
    }

    public long countAvailableForReservationUsingRoomCode(final Long roomCode, final String startDate,
        final String endDate) {
        AtomicLong remaining = new AtomicLong();
        List<Room> rooms = Optional.of(roomRepository.findByCode(roomCode)).orElseThrow();
        rooms.forEach(room -> {
            if (isAvailableForReservation(room.getId(), startDate, endDate)) {
                remaining.getAndIncrement();
            }
        });

        return remaining.get();
    }

    public boolean isAvailableForReservation(final Long roomId, final String startDate,
        final String endDate) {
        ValueOperations<String, Object> values = restTemplate.opsForValue();

        LocalDate startLocalDate = DateTimeUtil.toLocalDate(startDate);
        LocalDate endLocalDate = DateTimeUtil.toLocalDate(endDate);

        while (startLocalDate.isBefore(endLocalDate)) {

            String accommodationDate = DateTimeUtil.toString(startLocalDate);
            if (values.get("roomId:" + roomId + ":" + accommodationDate) != null) {
                return false;
            }
            startLocalDate = startLocalDate.plusDays(1);
        }

        return true;
    }


}
