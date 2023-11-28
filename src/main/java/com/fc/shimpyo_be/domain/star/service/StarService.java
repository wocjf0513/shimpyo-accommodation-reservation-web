package com.fc.shimpyo_be.domain.star.service;

import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.member.exception.MemberNotFoundException;
import com.fc.shimpyo_be.domain.member.repository.MemberRepository;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.exception.ProductNotFoundException;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.star.dto.request.StarRegisterRequestDto;
import com.fc.shimpyo_be.domain.star.dto.response.StarResponseDto;
import com.fc.shimpyo_be.domain.star.entity.Star;
import com.fc.shimpyo_be.domain.star.repository.StarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class StarService {

    private final StarRepository starRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public StarResponseDto register(Long memberId, StarRegisterRequestDto request) {
        log.info("{} ::: {}", getClass().getSimpleName(), "register");

        // 회원 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        // 상품 조회
        Product product = productRepository.findById(request.productId())
            .orElseThrow(ProductNotFoundException::new);

        // 별점 등록
        return new StarResponseDto(
          starRepository.save(
              Star.builder()
                  .member(member)
                  .product(product)
                  .score(request.score())
                  .build()
          )
        );
    }
}
