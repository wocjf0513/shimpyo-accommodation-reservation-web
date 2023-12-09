package com.fc.shimpyo_be.domain.star.service;

import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.member.exception.MemberNotFoundException;
import com.fc.shimpyo_be.domain.member.repository.MemberRepository;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.exception.ProductNotFoundException;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.reservationproduct.entity.ReservationProduct;
import com.fc.shimpyo_be.domain.reservationproduct.exception.ReservationProductNotFoundException;
import com.fc.shimpyo_be.domain.reservationproduct.repository.ReservationProductRepository;
import com.fc.shimpyo_be.domain.star.dto.request.StarRegisterRequestDto;
import com.fc.shimpyo_be.domain.star.dto.response.StarResponseDto;
import com.fc.shimpyo_be.domain.star.entity.Star;
import com.fc.shimpyo_be.domain.star.exception.CannotBeforeCheckOutException;
import com.fc.shimpyo_be.domain.star.repository.StarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class StarService {

    private final StarRepository starRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ReservationProductRepository reservationProductRepository;

    @Transactional
    public StarResponseDto register(Long memberId, StarRegisterRequestDto request) {
        log.debug("{} ::: {}", getClass().getSimpleName(), "register");

        // 회원 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        // 예약 숙소 조회
        ReservationProduct reservationProduct = reservationProductRepository.findByIdWithRoom(request.reservationProductId())
            .orElseThrow(ReservationProductNotFoundException::new);

        // 별점 등록 가능 기간인지 검증
        validateRegisterDate(LocalDateTime.now(), reservationProduct.getEndDate(), reservationProduct.getRoom().getCheckOut());

        // 숙소 조회
        Product product = productRepository.findById(request.productId())
            .orElseThrow(ProductNotFoundException::new);

        // 별점 총 수 카운트
        long total = starRepository.countByProduct(product);

        // 별점 평균 업데이트
        product.updateStarAvg(calculateStarAvg(product.getStarAvg(), total, request.score()));

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

    private float calculateStarAvg(float currentAvg, long total, float score) {
        float avg = ((currentAvg * total) + score) / (total + 1);

        return (float) (Math.round(avg * 10) / 10.0);
    }

    private void validateRegisterDate(LocalDateTime current, LocalDate endDate, LocalTime checkOut) {
        LocalDateTime target = LocalDateTime.of(endDate, checkOut);

        if (current.isEqual(target) || current.isBefore(target)) {
            log.error("별점 등록은 체크아웃 이후에 가능합니다.");
            throw new CannotBeforeCheckOutException();
        }
    }
}
