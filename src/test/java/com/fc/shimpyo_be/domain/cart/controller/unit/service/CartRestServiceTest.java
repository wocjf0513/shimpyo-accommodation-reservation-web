package com.fc.shimpyo_be.domain.cart.controller.unit.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.fc.shimpyo_be.domain.cart.controller.factory.CartFactory;
import com.fc.shimpyo_be.domain.cart.dto.request.CartCreateRequest;
import com.fc.shimpyo_be.domain.cart.dto.response.CartDeleteResponse;
import com.fc.shimpyo_be.domain.cart.dto.response.CartResponse;
import com.fc.shimpyo_be.domain.cart.entity.Cart;
import com.fc.shimpyo_be.domain.cart.repository.CartRepository;
import com.fc.shimpyo_be.domain.cart.service.CartService;
import com.fc.shimpyo_be.domain.cart.util.CartMapper;
import com.fc.shimpyo_be.domain.member.entity.Authority;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.member.repository.MemberRepository;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.factory.ProductFactory;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import com.fc.shimpyo_be.global.util.SecurityUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CartRestServiceTest {

    private static CartResponse cartResponse;
    private static Room room;
    private static Member member;
    private static Cart cart;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private SecurityUtil securityUtil;
    @InjectMocks
    private CartService cartService;


    @BeforeAll
    public static void initTest() {
        //given
        Product product = ProductFactory.createTestProduct();
        room = ProductFactory.createTestRoom(product);
        member = Member.builder().id(1L).email("wocjf0513@naver.com").photoUrl("hello,world.jpg")
            .name("심재철").password("1234").authority(Authority.ROLE_USER).build();
        cart = CartFactory.createCartTest(room, member);
        cartResponse = CartMapper.toCartResponse(cart);
    }

    @Test
    void SuccessToGetCarts() {
        //given
        List<Cart> carts = new ArrayList<>();
        carts.add(cart);
        List<CartResponse> expectedCartResponses = carts.stream().map(CartMapper::toCartResponse)
            .toList();
        given(cartRepository.findByMemberId(1L)).willReturn(Optional.of(carts));
        given(securityUtil.getCurrentMemberId()).willReturn(1L);
        //when
        List<CartResponse> cartResponses = cartService.getCarts();

        //then
        assertThat(cartResponses).usingRecursiveComparison().isEqualTo(expectedCartResponses);
    }

    @Test
    void SuccessToAddCart() {
        //given
        CartCreateRequest cartCreateRequest = CartCreateRequest.builder().roomId(room.getId())
            .price(100000L).startDate("2023-11-27").endDate("2023-11-28").build();
        Cart expectedCart = CartMapper.toCart(cartCreateRequest, room, member);
        CartResponse expectedCartResponse = CartMapper.toCartResponse(expectedCart);
        given(cartRepository.save(any())).willReturn(expectedCart);
        given(securityUtil.getCurrentMemberId()).willReturn(member.getId());
        given(roomRepository.findById(room.getId())).willReturn(Optional.ofNullable(room));
        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        //when
        CartResponse result = cartService.addCart(cartCreateRequest);
        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedCartResponse);
    }


    @Test
    void SuccessToDeleteCart() {
        //given
        given(securityUtil.getCurrentMemberId()).willReturn(member.getId());
        given(cartRepository.findById(cart.getId())).willReturn(Optional.ofNullable(cart));

        //when
        CartDeleteResponse cartDeleteResponse = cartService.deleteCart(cart.getId());
        //then
        assertThat(cartDeleteResponse).usingRecursiveComparison()
            .isEqualTo(CartMapper.toCartDeleteResponse(cart));
    }
}
