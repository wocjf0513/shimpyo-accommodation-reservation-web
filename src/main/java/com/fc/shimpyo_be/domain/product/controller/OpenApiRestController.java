package com.fc.shimpyo_be.domain.product.controller;

import com.fc.shimpyo_be.domain.product.service.OpenApiService;
import com.fc.shimpyo_be.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/open-api")
@RequiredArgsConstructor
public class OpenApiRestController {

    private final OpenApiService openApiService;

    @PostMapping
    ResponseEntity<ResponseDto<Void>> getDataFromOpenApi(
        @RequestParam int pageSize,
        @RequestParam int pageNum) {
        openApiService.getData(pageSize, pageNum);
        return ResponseEntity.status(HttpStatus.OK)
            .body(ResponseDto.res(HttpStatus.OK, "OpenAPI를 사용하여 성공적으로 데이터를 저장했습니다."));
    }
}
