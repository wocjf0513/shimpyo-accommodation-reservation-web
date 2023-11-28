package com.fc.shimpyo_be.domain.cart.unit.controller;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

import com.fc.shimpyo_be.domain.cart.controller.CartRestController;
import com.fc.shimpyo_be.domain.cart.factory.CartFactory;
import com.fc.shimpyo_be.domain.cart.dto.request.CartCreateRequest;
import com.fc.shimpyo_be.domain.cart.dto.response.CartDeleteResponse;
import com.fc.shimpyo_be.domain.cart.dto.response.CartResponse;
import com.fc.shimpyo_be.domain.cart.entity.Cart;
import com.fc.shimpyo_be.domain.cart.service.CartService;
import com.fc.shimpyo_be.domain.cart.util.CartMapper;
import com.fc.shimpyo_be.domain.member.entity.Authority;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.factory.ProductFactory;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.global.common.ResponseDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
@AutoConfigureMockMvc
public class CartRestControllerTest {

    private static List<CartResponse> cartResponses = new ArrayList<>();
    @MockBean
    private CartService cartService;
    @Autowired
    private CartRestController cartRestController;
    private static CartResponse cartResponse;
    private static Room room;
    private static Member member;

    private static Cart cart;
    @BeforeAll
    public static void initTest() {
        //given
        Product product = ProductFactory.createTestProduct();
        room = ProductFactory.createTestRoom(product);
        member = Member.builder().email("wocjf0513@naver.com").photoUrl("hello,world.jpg")
            .name("심재철").password("1234").authority(Authority.ROLE_USER).build();
        cart = CartFactory.createCartTest(room, member);
        cartResponse = CartMapper.toCartResponse(cart);
        cartResponses.add(cartResponse);
    }

    @Test
    void successToGetCarts() {
        //given
        given(cartService.getCarts()).willReturn(cartResponses);
        //when
        ResponseEntity<ResponseDto<List<CartResponse>>> results= cartRestController.getCarts();
        //then
        assertEquals(results.getStatusCode(), HttpStatus.OK);
        List<CartResponse> resultCartResponses= results.getBody().getData();
        assertThat(resultCartResponses).usingRecursiveComparison().isEqualTo(cartResponses);
    }

    @Test
    void successToAddCart() {
        //given
        CartCreateRequest cartCreateRequest = CartCreateRequest.builder().roomId(room.getId()).price(cartResponse.getPrice())
            .startDate(cartResponse.getStartDate()).endDate(cartResponse.getEndDate())
            .build();

        given(cartService.addCart(cartCreateRequest)).willReturn(cartResponse);
        //when
        ResponseEntity<ResponseDto<CartResponse>> result= cartRestController.addCart(cartCreateRequest);
        //then
        assertEquals(result.getStatusCode(), HttpStatus.OK);
        CartResponse resultCartResponse = result.getBody().getData();
        assertThat(resultCartResponse).usingRecursiveComparison().isEqualTo(cartResponse);
    }


    @Test
    void successToDeleteCart() {
        //given
        CartDeleteResponse expectedCartDeleteResponse= CartMapper.toCartDeleteResponse(cart);
        given(cartService.deleteCart(cartResponse.getCartId())).willReturn(
            CartMapper.toCartDeleteResponse(cart));
        //when
        ResponseEntity<ResponseDto<CartDeleteResponse>> cartDeleteResponse = cartRestController.deleteCart(cartResponse.getCartId());
        //then
        assertNotNull(cartDeleteResponse);
        assertEquals(cartDeleteResponse.getStatusCode(),HttpStatus.OK);
        CartDeleteResponse resultCartDeleteResponse = cartDeleteResponse.getBody().getData();
        assertThat(resultCartDeleteResponse).usingRecursiveComparison().isEqualTo(expectedCartDeleteResponse);
    }

}
