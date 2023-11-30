package com.fc.shimpyo_be.domain.member.controller;

import com.fc.shimpyo_be.domain.member.dto.request.RefreshRequestDto;
import com.fc.shimpyo_be.domain.member.dto.request.SignInRequestDto;
import com.fc.shimpyo_be.domain.member.dto.request.SignUpRequestDto;
import com.fc.shimpyo_be.domain.member.dto.response.MemberResponseDto;
import com.fc.shimpyo_be.domain.member.dto.response.SignInResponseDto;
import com.fc.shimpyo_be.domain.member.service.AuthService;
import com.fc.shimpyo_be.global.common.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseDto<MemberResponseDto>> signUp(
        @Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        log.debug("email: {}, name: {}", signUpRequestDto.getEmail(), signUpRequestDto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ResponseDto.res(HttpStatus.CREATED, authService.signUp(signUpRequestDto),
                "성공적으로 회원가입 했습니다."));
    }

    @PostMapping("/signin")
    public ResponseEntity<ResponseDto<SignInResponseDto>> signIn(
        @Valid @RequestBody SignInRequestDto signInRequestDto) {
        log.debug("email: {}", signInRequestDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDto.res(HttpStatus.OK, authService.signIn(signInRequestDto),
                "성공적으로 로그인 했습니다."));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseDto<SignInResponseDto>> refresh(
        @Valid @RequestBody RefreshRequestDto refreshRequestDto) {
        log.debug("accessToken: {}, refreshToken: {}", refreshRequestDto.getAccessToken(),
            refreshRequestDto.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDto.res(HttpStatus.OK, authService.refresh(refreshRequestDto),
                "성공적으로 토큰을 재발급 했습니다."));
    }
}
