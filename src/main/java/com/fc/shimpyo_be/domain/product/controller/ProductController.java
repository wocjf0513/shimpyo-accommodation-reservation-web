package com.fc.shimpyo_be.domain.product.controller;

import com.fc.shimpyo_be.domain.product.dto.response.ProductResponse;
import com.fc.shimpyo_be.domain.product.service.ProductService;
import com.fc.shimpyo_be.global.common.ResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("")
    public ResponseDto<List<ProductResponse>> getAllProducts(
        @RequestParam(required = false, defaultValue = "") String keyword,
        @PageableDefault(size = 10, page = 0) Pageable pageable ) {

        return ResponseDto.res(HttpStatus.OK, productService.getAllProducts(keyword, pageable),
            "상품 목록을 성공적으로 조회했습니다.");
    }

    /*ResponseDto<List<ProductDetailsResponse>> getProductDetails() {

    }*/
}
