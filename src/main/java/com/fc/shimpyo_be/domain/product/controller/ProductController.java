package com.fc.shimpyo_be.domain.product.controller;

import com.fc.shimpyo_be.domain.product.PageableConstraint;
import com.fc.shimpyo_be.domain.product.dto.response.ProductDetailsResponse;
import com.fc.shimpyo_be.domain.product.dto.response.ProductResponse;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.exception.ProductNotFoundException;
import com.fc.shimpyo_be.domain.product.service.ProductService;
import com.fc.shimpyo_be.global.common.ResponseDto;
import com.fc.shimpyo_be.global.util.DateTimeUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
public class ProductController {

    private final ProductService productService;

    @GetMapping("")
    public ResponseDto<List<ProductResponse>> getAllProducts(
        @RequestParam(required = false, defaultValue = "") String keyword,
        @PageableConstraint(Product.class) @PageableDefault(size = 10, page =  0)  Pageable pageable ) {
        //pageable에 잘못된 값이 왔을 때의 처리가 필요함.
        return ResponseDto.res(HttpStatus.OK, productService.getAllProducts(keyword, pageable),
            "상품 목록을 성공적으로 조회했습니다.");
    }


//    @GetMapping("/{productId}")
//    ResponseDto<ProductDetailsResponse> getProductDetails(@PathVariable("productId") Long productId,
//        @RequestParam @Pattern(regexp = DateTimeUtil.LOCAL_DATE_REGEX_PATTERN, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25)") String startDate,
//        @RequestParam @Pattern(regexp = DateTimeUtil.LOCAL_DATE_REGEX_PATTERN, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25)") String endDate) {
//
//        return ResponseDto.res(HttpStatus.OK, productService.getProductDetails(productId, startDate, endDate),"상품을 성공적으로 죄회했습니다.");
//    }
//
//    @GetMapping("/amounts/{roomId}")
//    ResponseDto<Void> isAvailableForReservation (@PathVariable("roomId") Long roomId,
//        @RequestParam @Pattern(regexp = DateTimeUtil.LOCAL_DATE_REGEX_PATTERN, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25)") String startDate,
//        @RequestParam @Pattern(regexp = DateTimeUtil.LOCAL_DATE_REGEX_PATTERN, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25)") String endDate) {
//        if(productService.isAvailableForReservation(roomId, startDate, endDate)){
//            return ResponseDto.res(HttpStatus.OK,"예약 가능한 상품입니다.");
//        }
//            throw new ProductNotFoundException();
//    }
}
