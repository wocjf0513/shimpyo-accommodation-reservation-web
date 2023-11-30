package com.fc.shimpyo_be.domain.product.controller;

import com.fc.shimpyo_be.domain.product.dto.request.SearchKeywordRequest;
import com.fc.shimpyo_be.domain.product.dto.response.ProductDetailsResponse;
import com.fc.shimpyo_be.domain.product.dto.response.ProductResponse;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.exception.InvalidDateException;
import com.fc.shimpyo_be.domain.product.exception.RoomNotReserveException;
import com.fc.shimpyo_be.domain.product.service.ProductService;
import com.fc.shimpyo_be.domain.product.util.model.PageableConstraint;
import com.fc.shimpyo_be.global.common.ResponseDto;
import com.fc.shimpyo_be.global.util.DateTimeUtil;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
public class ProductRestController {

    private final ProductService productService;


    @GetMapping
    public ResponseEntity<ResponseDto<List<ProductResponse>>> getProducts(
        @RequestParam(required = false) String productName,
        @RequestParam(required = false) String address,
        @RequestParam(required = false) String category,
        @PageableConstraint(Product.class) @PageableDefault(size = 10, page = 0) Pageable pageable) {

        SearchKeywordRequest searchKeywordRequest = SearchKeywordRequest.builder()
            .productName(productName).address(address).category(category).build();

        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK,
            productService.getProducts(searchKeywordRequest, pageable), "상품 목록을 성공적으로 조회했습니다."));
    }


    @GetMapping("/{productId}")
    ResponseEntity<ResponseDto<ProductDetailsResponse>> getProductDetails(
        @PathVariable("productId") Long productId,
        @RequestParam @Pattern(regexp = DateTimeUtil.LOCAL_DATE_REGEX_PATTERN, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25)") String startDate,
        @RequestParam @Pattern(regexp = DateTimeUtil.LOCAL_DATE_REGEX_PATTERN, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25)") String endDate) {

        if (DateTimeUtil.isNotValidDate(DateTimeUtil.toLocalDate(startDate),
            DateTimeUtil.toLocalDate(endDate))) {
            throw new InvalidDateException();
        }

        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK,
            productService.getProductDetails(productId, startDate, endDate), "상품을 성공적으로 조회했습니다."));
    }

    @GetMapping("/amounts/{roomId}")
    ResponseEntity<ResponseDto<Void>> isAvailableForReservation(@PathVariable("roomId") Long roomId,
        @RequestParam @Pattern(regexp = DateTimeUtil.LOCAL_DATE_REGEX_PATTERN, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25)") String startDate,
        @RequestParam @Pattern(regexp = DateTimeUtil.LOCAL_DATE_REGEX_PATTERN, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25)") String endDate) {

        if (DateTimeUtil.isNotValidDate(DateTimeUtil.toLocalDate(startDate),
            DateTimeUtil.toLocalDate(endDate))) {
            throw new InvalidDateException();
        }

        if (productService.isAvailableForReservation(roomId, startDate, endDate)) {
            return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "예약 가능한 방입니다."));
        }
        throw new RoomNotReserveException();
    }
}
