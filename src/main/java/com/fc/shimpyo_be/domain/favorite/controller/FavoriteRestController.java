package com.fc.shimpyo_be.domain.favorite.controller;

import com.fc.shimpyo_be.domain.favorite.dto.FavoriteResponseDto;
import com.fc.shimpyo_be.domain.favorite.service.FavoriteService;
import com.fc.shimpyo_be.global.common.ResponseDto;
import com.fc.shimpyo_be.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class FavoriteRestController {

    private final FavoriteService favoriteService;
    private final SecurityUtil securityUtil;

    @PostMapping("/{productId}")
    public ResponseEntity<ResponseDto<FavoriteResponseDto>> register(@PathVariable long productId) {
        log.debug("memberId: {}, productId: {}", securityUtil.getCurrentMemberId(), productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.res(HttpStatus.CREATED,
            favoriteService.register(securityUtil.getCurrentMemberId(), productId),
            "성공적으로 즐겨찾기를 등록했습니다."));
    }
}
