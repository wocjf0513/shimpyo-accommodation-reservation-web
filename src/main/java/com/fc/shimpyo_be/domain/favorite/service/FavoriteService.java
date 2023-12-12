package com.fc.shimpyo_be.domain.favorite.service;

import com.fc.shimpyo_be.domain.favorite.dto.FavoriteResponseDto;
import com.fc.shimpyo_be.domain.favorite.entity.Favorite;
import com.fc.shimpyo_be.domain.favorite.exception.FavoriteAlreadyRegisterException;
import com.fc.shimpyo_be.domain.favorite.repository.FavoriteRepository;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.member.service.MemberService;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.exception.ProductNotFoundException;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final ProductRepository productRepository;

    public FavoriteResponseDto register(long memberId, long productId) {
        Member member = memberService.getMemberById(memberId);
        Product product = productRepository.findById(productId).orElseThrow(
            ProductNotFoundException::new);
        Optional<Favorite> favorite = favoriteRepository.findByMemberAndProduct(member, product);
        if (favorite.isEmpty()) {
            throw new FavoriteAlreadyRegisterException();
        }
        return FavoriteResponseDto.of(favoriteRepository.save(Favorite.builder()
            .member(member)
            .product(product)
            .build()));
    }
}
