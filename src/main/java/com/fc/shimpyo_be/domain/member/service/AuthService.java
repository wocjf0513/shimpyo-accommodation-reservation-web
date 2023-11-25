package com.fc.shimpyo_be.domain.member.service;

import com.fc.shimpyo_be.domain.member.dto.request.RefreshRequestDto;
import com.fc.shimpyo_be.domain.member.dto.request.SignInRequestDto;
import com.fc.shimpyo_be.domain.member.dto.request.SignUpRequestDto;
import com.fc.shimpyo_be.domain.member.dto.response.MemberResponseDto;
import com.fc.shimpyo_be.domain.member.dto.response.SignInResponseDto;
import com.fc.shimpyo_be.domain.member.dto.response.TokenResponseDto;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.member.entity.RefreshToken;
import com.fc.shimpyo_be.domain.member.exception.AlreadyExistsMemberException;
import com.fc.shimpyo_be.domain.member.exception.InvalidRefreshTokenException;
import com.fc.shimpyo_be.domain.member.exception.LoggedOutException;
import com.fc.shimpyo_be.domain.member.exception.UnmatchedMemberException;
import com.fc.shimpyo_be.domain.member.repository.RefreshTokenRepository;
import com.fc.shimpyo_be.global.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberService memberService;

    public MemberResponseDto signUp(SignUpRequestDto signUpRequestDto) {
        if (memberService.isExistsByEmail(signUpRequestDto.getEmail())) {
            throw new AlreadyExistsMemberException();
        }
        memberService.checkValidPassword(signUpRequestDto.getPassword());
        memberService.checkMatchedPassword(signUpRequestDto.getPassword(),
            signUpRequestDto.getPasswordConfirm());
        Member member = signUpRequestDto.toMember(passwordEncoder);
        return MemberResponseDto.of(memberService.saveMember(member));
    }

    public SignInResponseDto signIn(SignInRequestDto signInRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = signInRequestDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject()
            .authenticate(authenticationToken);
        TokenResponseDto tokenResponseDto = jwtTokenProvider.generateToken(authentication);
        RefreshToken refreshToken = RefreshToken.builder()
            .id(Long.parseLong(authentication.getName()))
            .token(tokenResponseDto.getRefreshToken())
            .build();
        refreshTokenRepository.save(refreshToken);
        return SignInResponseDto.builder().member(MemberResponseDto.of(
                memberService.getMember(refreshToken.getId())))
            .token(tokenResponseDto).build();
    }

    public SignInResponseDto refresh(RefreshRequestDto refreshRequestDto) {
        if (!jwtTokenProvider.validateToken(refreshRequestDto.getRefreshToken())) {
            throw new InvalidRefreshTokenException();
        }
        Authentication authentication = jwtTokenProvider.getAuthentication(
            refreshRequestDto.getAccessToken());
        RefreshToken refreshToken = refreshTokenRepository.findById(
            Long.parseLong(authentication.getName())).orElseThrow(LoggedOutException::new);
        if (!refreshToken.getToken().equals(refreshRequestDto.getRefreshToken())) {
            throw new UnmatchedMemberException();
        }
        TokenResponseDto tokenResponseDto = jwtTokenProvider.generateToken(authentication);
        refreshToken.updateValue(tokenResponseDto.getRefreshToken());
        return SignInResponseDto.builder()
            .member(MemberResponseDto.of(memberService.getMember(refreshToken.getId())))
            .token(tokenResponseDto)
            .build();
    }
}
