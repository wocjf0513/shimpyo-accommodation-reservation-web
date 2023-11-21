package com.fc.shimpyo_be.domain.product.controller;

import com.fc.shimpyo_be.domain.product.dto.request.PagenationRequest;
import com.fc.shimpyo_be.domain.product.dto.response.ProductResponse;
import com.fc.shimpyo_be.domain.product.service.ProductService;
import com.fc.shimpyo_be.global.common.ResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/")
    public ResponseDto<List<ProductResponse>> getAllProducts(
        @RequestBody PagenationRequest pagenationRequest) {
        Pageable pageable = PageRequest.of(pagenationRequest.getPageNo().intValue(),
            pagenationRequest.getPageSize().intValue());

        return ResponseDto.res(HttpStatus.OK, productService.getAllProducts(pageable),
            "상품 목록을 성공적으로 조회했습니다.");
    }

//    ResponseDto<List<ProductDetailsResponse>> getProductDetails() {
//
//    }
}
