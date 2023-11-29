package com.fc.shimpyo_be.domain.cart.controller;

import com.fc.shimpyo_be.domain.cart.dto.request.CartCreateRequest;
import com.fc.shimpyo_be.domain.cart.dto.response.CartDeleteResponse;
import com.fc.shimpyo_be.domain.cart.dto.response.CartResponse;
import com.fc.shimpyo_be.domain.cart.service.CartService;
import com.fc.shimpyo_be.domain.product.exception.InvalidDateException;
import com.fc.shimpyo_be.global.common.ResponseDto;
import com.fc.shimpyo_be.global.util.DateTimeUtil;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartRestController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ResponseDto<List<CartResponse>>> getCarts() {
        return ResponseEntity.ok().body(ResponseDto.res(HttpStatus.OK,cartService.getCarts(),"장바구니 목록을 성공적으로 조회했습니다."));
    }

    @PostMapping
    public ResponseEntity<ResponseDto<CartResponse>> addCart(
        @Valid @RequestBody CartCreateRequest cartCreateRequest) {
        if(DateTimeUtil.isNotValidDate(DateTimeUtil.toLocalDate(cartCreateRequest.startDate()),DateTimeUtil.toLocalDate(cartCreateRequest.endDate()))){
            throw new InvalidDateException();
        }
        return ResponseEntity.ok().body(ResponseDto.res(HttpStatus.OK,cartService.addCart(cartCreateRequest),"장바구니를 성공적으로 등록했습니다."));
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<ResponseDto<CartDeleteResponse>> deleteCart(@PathVariable("cartId") Long cartId) {
        return ResponseEntity.ok().body(ResponseDto.res(HttpStatus.OK,cartService.deleteCart(cartId),"장바구니를 성공적으로 삭제했습니다."));
    }
}
