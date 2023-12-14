package com.fc.shimpyo_be.domain.favorite.controller;

import com.fc.shimpyo_be.domain.favorite.dto.FavoriteResponseDto;
import com.fc.shimpyo_be.domain.favorite.dto.FavoritesResponseDto;
import com.fc.shimpyo_be.domain.favorite.entity.Favorite;
import com.fc.shimpyo_be.domain.favorite.service.FavoriteService;
import com.fc.shimpyo_be.domain.product.util.model.PageableConstraint;
import com.fc.shimpyo_be.global.common.ResponseDto;
import com.fc.shimpyo_be.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class FavoriteRestController {

    private final FavoriteService favoriteService;
    private final SecurityUtil securityUtil;
    private static final int DEFAULT_SIZE = 10;
    private static final int DEFAULT_PAGE = 0;

    @PostMapping("/{productId}")
    public ResponseEntity<ResponseDto<FavoriteResponseDto>> register(@PathVariable long productId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.res(HttpStatus.CREATED,
            favoriteService.register(securityUtil.getCurrentMemberId(), productId),
            "성공적으로 즐겨찾기를 등록했습니다."));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<FavoritesResponseDto>> getFavorites(
        @PageableConstraint(Favorite.class) @PageableDefault(size = DEFAULT_SIZE, page = DEFAULT_PAGE) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(ResponseDto.res(HttpStatus.OK, favoriteService.getFavorites(
                securityUtil.getCurrentMemberId(), pageable), "성공적으로 즐겨찾기 목록을 조회했습니다."));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ResponseDto<FavoriteResponseDto>> cancel(@PathVariable long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.res(HttpStatus.OK,
            favoriteService.delete(securityUtil.getCurrentMemberId(), productId),
            "성공적으로 즐겨찾기를 취소했습니다."));
    }
}
