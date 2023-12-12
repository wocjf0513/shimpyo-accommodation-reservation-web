package com.fc.shimpyo_be.domain.favorite.service;

import com.fc.shimpyo_be.domain.favorite.dto.FavoriteResponseDto;
import com.fc.shimpyo_be.domain.favorite.entity.Favorite;
import com.fc.shimpyo_be.domain.favorite.exception.FavoriteAlreadyRegisterException;
import com.fc.shimpyo_be.domain.favorite.exception.FavoriteNotFoundException;
import com.fc.shimpyo_be.domain.favorite.repository.FavoriteRepository;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.member.service.MemberService;
import com.fc.shimpyo_be.domain.product.dto.response.ProductResponse;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.exception.ProductNotFoundException;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.product.util.ProductMapper;
import java.util.ArrayList;
import java.util.List;
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
        if (favorite.isPresent()) {
            throw new FavoriteAlreadyRegisterException();
        }
        return FavoriteResponseDto.of(favoriteRepository.save(Favorite.builder()
            .member(member)
            .product(product)
            .build()));
    }

    public List<ProductResponse> getFavorites(long memberId) {
        List<ProductResponse> productResponses = new ArrayList<>();
        Member member = memberService.getMemberById(memberId);
        List<Favorite> favorites = favoriteRepository.findByMember(member);
        for (Favorite favorite : favorites) {
            productResponses.add(ProductMapper.toProductResponse(favorite.getProduct()));
        }
        return productResponses;
    }

    public FavoriteResponseDto delete(long memberId, long productId) {
        Member member = memberService.getMemberById(memberId);
        Product product = productRepository.findById(productId).orElseThrow(
            ProductNotFoundException::new);
        Favorite favorite = favoriteRepository.findByMemberAndProduct(member, product)
            .orElseThrow(FavoriteNotFoundException::new);
        FavoriteResponseDto favoriteResponseDto = FavoriteResponseDto.of(favorite);
        favoriteRepository.delete(favorite);
        return favoriteResponseDto;
    }
}
