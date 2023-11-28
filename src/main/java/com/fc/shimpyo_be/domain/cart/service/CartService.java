package com.fc.shimpyo_be.domain.cart.service;

import com.fc.shimpyo_be.domain.cart.dto.request.CartCreateRequest;
import com.fc.shimpyo_be.domain.cart.dto.response.CartDeleteResponse;
import com.fc.shimpyo_be.domain.cart.dto.response.CartResponse;
import com.fc.shimpyo_be.domain.cart.entity.Cart;
import com.fc.shimpyo_be.domain.cart.exception.CartNotDeleteException;
import com.fc.shimpyo_be.domain.cart.exception.CartNotFoundException;
import com.fc.shimpyo_be.domain.cart.repository.CartRepository;
import com.fc.shimpyo_be.domain.cart.util.CartMapper;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.member.exception.MemberNotFoundException;
import com.fc.shimpyo_be.domain.member.repository.MemberRepository;
import com.fc.shimpyo_be.domain.product.exception.RoomNotFoundException;
import com.fc.shimpyo_be.domain.product.exception.RoomNotReserveException;
import com.fc.shimpyo_be.domain.product.service.ProductService;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import com.fc.shimpyo_be.global.util.SecurityUtil;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;

    private final RoomRepository roomRepository;

    private final SecurityUtil securityUtil;

    private final MemberRepository memberRepository;

    private final ProductService productService;

    public List<CartResponse> getCarts() {
        List<CartResponse> cartResponses= cartRepository.findByMemberId(securityUtil.getCurrentMemberId()).orElseThrow()
            .stream().map(CartMapper::toCartResponse).toList();
        cartResponses.stream().filter(
            cartResponse -> !productService.isAvailableForReservation(cartResponse.getRoomId(),
                cartResponse.getStartDate(), cartResponse.getEndDate())).forEach(
            CartResponse::setReserved);

        return cartResponses;
    }

    @Transactional
    public CartResponse addCart(@Valid @RequestBody CartCreateRequest cartCreateRequest) {

        Member member = memberRepository.findById(securityUtil.getCurrentMemberId())
            .orElseThrow(MemberNotFoundException::new);

        if (!productService.isAvailableForReservation(cartCreateRequest.roomId(),
            cartCreateRequest.startDate(), cartCreateRequest.endDate())) {
            throw new RoomNotReserveException();
        }

        Room room = roomRepository.findById(cartCreateRequest.roomId())
            .orElseThrow(RoomNotFoundException::new);
        Cart createdCart = cartRepository.save(CartMapper.toCart(cartCreateRequest, room, member));
        return CartMapper.toCartResponse(createdCart);
    }

    @Transactional
    public CartDeleteResponse deleteCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);

        if (!cart.getMember().getId().equals(securityUtil.getCurrentMemberId())) {
            throw new CartNotDeleteException();
        }
        cartRepository.deleteById(cart.getId());

        return CartMapper.toCartDeleteResponse(cart);
    }


}
