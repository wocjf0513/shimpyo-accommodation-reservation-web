package com.fc.shimpyo_be.domain.member.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fc.shimpyo_be.domain.member.dto.request.RefreshRequestDto;
import com.fc.shimpyo_be.domain.member.dto.request.SignInRequestDto;
import com.fc.shimpyo_be.domain.member.dto.request.SignUpRequestDto;
import com.fc.shimpyo_be.domain.member.dto.response.MemberResponseDto;
import com.fc.shimpyo_be.domain.member.dto.response.SignInResponseDto;
import com.fc.shimpyo_be.domain.member.dto.response.TokenResponseDto;
import com.fc.shimpyo_be.domain.member.entity.Authority;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.member.entity.RefreshToken;
import com.fc.shimpyo_be.domain.member.exception.AlreadyExistsMemberException;
import com.fc.shimpyo_be.domain.member.exception.InvalidRefreshTokenException;
import com.fc.shimpyo_be.domain.member.exception.UnmatchedMemberException;
import com.fc.shimpyo_be.domain.member.exception.UnmatchedPasswordException;
import com.fc.shimpyo_be.domain.member.repository.RefreshTokenRepository;
import com.fc.shimpyo_be.domain.member.service.AuthService;
import com.fc.shimpyo_be.domain.member.service.MemberService;
import com.fc.shimpyo_be.global.config.jwt.JwtTokenProvider;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Nested
    @DisplayName("signUp()은")
    class Context_signUp {

        @Test
        @DisplayName("회원 가입을 할 수 있다.")
        void _willSuccess() {
            // given
            SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .email("test@mail.com")
                .name("test")
                .password("qwer1234$$")
                .passwordConfirm("qwer1234$$")
                .build();
            Member member = Member.builder()
                .id(1L)
                .email("test@mail.com")
                .name("test")
                .password("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm")
                .photoUrl("")
                .authority(Authority.ROLE_USER)
                .build();

            given(memberService.isExistsByEmail(any(String.class))).willReturn(false);
            given(passwordEncoder.encode(any(String.class)))
                .willReturn("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm");
            given(memberService.saveMember(any(Member.class))).willReturn(member);

            // when
            MemberResponseDto result = authService.signUp(signUpRequestDto);

            // then
            assertNotNull(result);
            assertThat(result).extracting("memberId", "email", "name", "photoUrl")
                .containsExactly(1L, "test@mail.com", "test", "");

            verify(memberService, times(1)).isExistsByEmail(any(String.class));
            verify(passwordEncoder, times(1)).encode(any(String.class));
            verify(memberService, times(1)).saveMember(any(Member.class));
        }

        @Test
        @DisplayName("이미 회원 가입한 회원은 회원 가입을 할 수 없다.")
        void alreadyExistsMember_willFail() {
            // given
            SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .email("test@mail.com")
                .name("test")
                .password("qwer1234$$")
                .passwordConfirm("qwer1234$$")
                .build();

            given(memberService.isExistsByEmail(any(String.class))).willReturn(true);

            // when
            Throwable exception = assertThrows(AlreadyExistsMemberException.class, () -> {
                authService.signUp(signUpRequestDto);
            });

            // then
            assertEquals("이미 가입된 회원 입니다.", exception.getMessage());

            verify(memberService, times(1)).isExistsByEmail(any(String.class));
            verify(memberService, never()).saveMember(any(Member.class));
        }

        @Test
        @DisplayName("비밀번호와 비밀번호 확인이 일치하지 않으면 회원 가입을 할 수 없다.")
        void unmatchedPassword_willFail() {
            // given
            SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .email("test@mail.com")
                .name("test")
                .password("qwer1234$$")
                .passwordConfirm("qwer1234$")
                .build();

            given(memberService.isExistsByEmail(any(String.class))).willReturn(false);
            doThrow(new UnmatchedPasswordException()).when(memberService)
                .checkMatchedPassword(any(String.class), any(String.class));
            // when
            Throwable exception = assertThrows(UnmatchedPasswordException.class, () -> {
                authService.signUp(signUpRequestDto);
            });

            // then
            assertEquals("비밀번호와 비밀번호 확인이 일치하지 않습니다.", exception.getMessage());

            verify(memberService, times(1)).isExistsByEmail(any(String.class));
            verify(memberService, never()).saveMember(any(Member.class));
        }

    }

    @Nested
    @DisplayName("signIn()은")
    class Context_signIn {

        @Test
        @DisplayName("로그인을 할 수 있다.")
        void _willSuccess() {
            // given
            SignInRequestDto signInRequestDto = SignInRequestDto.builder()
                .email("test@mail.com")
                .password("qwer1234$$")
                .build();
            Member member = Member.builder()
                .id(1L)
                .email("test@mail.com")
                .name("test")
                .password("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm")
                .photoUrl(
                    "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI")
                .authority(Authority.ROLE_USER)
                .build();
            UserDetails principal = new User(String.valueOf(member.getId()), "",
                Arrays.stream(new String[]{member.getAuthority().name()})
                    .map(SimpleGrantedAuthority::new)
                    .toList());
            Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "",
                Arrays.stream(new String[]{member.getAuthority().name()})
                    .map(SimpleGrantedAuthority::new)
                    .toList());
            TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .grantType("Bearer")
                .accessToken(
                    "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTcwMDU4NjkyOH0.lof7WjalCH1gGPy2q7YYi9VTcgn_aoFMwEMQvITtddsUIcJN-YzNODt_RQde5J5dH98NKMXDOvy7YwNlt6BCfg")
                .accessTokenExpiresIn(1700586928520L)
                .refreshToken(
                    "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MDExODk5Mjh9.uZuIAxsnf4Ubz5K9YzysJTu9Gh25XNTsPVAPSElw1lS78gS8S08L97Z4RkfGodegGXZ9UFFNkVXdhRzF9Pr-uA")
                .build();
            RefreshToken refreshToken = RefreshToken.builder()
                .id(1L)
                .token(
                    "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MDExODk5Mjh9.uZuIAxsnf4Ubz5K9YzysJTu9Gh25XNTsPVAPSElw1lS78gS8S08L97Z4RkfGodegGXZ9UFFNkVXdhRzF9Pr-uA")
                .build();

            given(authenticationManagerBuilder.getObject()).willReturn(authenticationManager);
            given(authenticationManager.authenticate(any(Authentication.class))).willReturn(
                authentication);
            given(jwtTokenProvider.generateToken(any(Authentication.class))).willReturn(
                tokenResponseDto);
            given(refreshTokenRepository.save(any(RefreshToken.class))).willReturn(refreshToken);
            given(memberService.getMember(any(long.class))).willReturn(member);

            // when
            SignInResponseDto result = authService.signIn(signInRequestDto);

            // then
            assertNotNull(result);
            assertThat(result.getMember()).extracting("memberId", "email", "name", "photoUrl")
                .containsExactly(1L, "test@mail.com", "test",
                    "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI");
            assertThat(result.getToken()).extracting("grantType", "accessToken",
                    "accessTokenExpiresIn", "refreshToken")
                .containsExactly("Bearer",
                    "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTcwMDU4NjkyOH0.lof7WjalCH1gGPy2q7YYi9VTcgn_aoFMwEMQvITtddsUIcJN-YzNODt_RQde5J5dH98NKMXDOvy7YwNlt6BCfg",
                    1700586928520L,
                    "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MDExODk5Mjh9.uZuIAxsnf4Ubz5K9YzysJTu9Gh25XNTsPVAPSElw1lS78gS8S08L97Z4RkfGodegGXZ9UFFNkVXdhRzF9Pr-uA");

            verify(authenticationManagerBuilder, times(1)).getObject();
            verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
            verify(jwtTokenProvider, times(1)).generateToken(any(Authentication.class));
            verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
            verify(memberService, times(1)).getMember(any(long.class));
        }
    }

    @Nested
    @DisplayName("refresh()은")
    class Context_refresh {

        @Test
        @DisplayName("토큰을 재발급 할 수 있다.")
        void _willSuccess() {
            // given
            RefreshRequestDto refreshRequestDto = RefreshRequestDto.builder()
                .accessToken(
                    "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTcwMDU4NjkyOH0.lof7WjalCH1gGPy2q7YYi9VTcgn_aoFMwEMQvITtddsUIcJN-YzNODt_RQde5J5dH98NKMXDOvy7YwNlt6BCfg")
                .refreshToken(
                    "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MDExODk5Mjh9.uZuIAxsnf4Ubz5K9YzysJTu9Gh25XNTsPVAPSElw1lS78gS8S08L97Z4RkfGodegGXZ9UFFNkVXdhRzF9Pr-uA")
                .build();
            Member member = Member.builder()
                .id(1L)
                .email("test@mail.com")
                .name("test")
                .password("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm")
                .photoUrl(
                    "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI")
                .authority(Authority.ROLE_USER)
                .build();
            UserDetails principal = new User(String.valueOf(member.getId()), "",
                Arrays.stream(new String[]{member.getAuthority().name()})
                    .map(SimpleGrantedAuthority::new)
                    .toList());
            Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "",
                Arrays.stream(new String[]{member.getAuthority().name()})
                    .map(SimpleGrantedAuthority::new)
                    .toList());
            TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .grantType("Bearer")
                .accessToken(
                    "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTcwMDU4NjkyOH0.lof7WjalCH1gGPy2q7YYi9VTcgn_aoFMwEMQvITtddsUIcJN-YzNODt_RQde5J5dH98NKMXDOvy7YwNlt6BCfg")
                .accessTokenExpiresIn(1700586928520L)
                .refreshToken(
                    "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MDExODk5Mjh9.uZuIAxsnf4Ubz5K9YzysJTu9Gh25XNTsPVAPSElw1lS78gS8S08L97Z4RkfGodegGXZ9UFFNkVXdhRzF9Pr-uA")
                .build();
            RefreshToken refreshToken = RefreshToken.builder()
                .id(1L)
                .token(
                    "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MDExODk5Mjh9.uZuIAxsnf4Ubz5K9YzysJTu9Gh25XNTsPVAPSElw1lS78gS8S08L97Z4RkfGodegGXZ9UFFNkVXdhRzF9Pr-uA")
                .build();

            given(jwtTokenProvider.validateToken(any(String.class))).willReturn(true);
            given(jwtTokenProvider.getAuthentication(any(String.class))).willReturn(authentication);
            given(refreshTokenRepository.findById(any(Long.class))).willReturn(
                Optional.of(refreshToken));
            given(jwtTokenProvider.generateToken(any(Authentication.class))).willReturn(
                tokenResponseDto);
            given(memberService.getMember(any(long.class))).willReturn(member);

            // when
            SignInResponseDto result = authService.refresh(refreshRequestDto);

            // then
            assertNotNull(result);
            assertThat(result.getMember()).extracting("memberId", "email", "name", "photoUrl")
                .containsExactly(1L, "test@mail.com", "test",
                    "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI");
            assertThat(result.getToken()).extracting("grantType", "accessToken",
                    "accessTokenExpiresIn", "refreshToken")
                .containsExactly("Bearer",
                    "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTcwMDU4NjkyOH0.lof7WjalCH1gGPy2q7YYi9VTcgn_aoFMwEMQvITtddsUIcJN-YzNODt_RQde5J5dH98NKMXDOvy7YwNlt6BCfg",
                    1700586928520L,
                    "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MDExODk5Mjh9.uZuIAxsnf4Ubz5K9YzysJTu9Gh25XNTsPVAPSElw1lS78gS8S08L97Z4RkfGodegGXZ9UFFNkVXdhRzF9Pr-uA");

            verify(jwtTokenProvider, times(1)).validateToken(any(String.class));
            verify(jwtTokenProvider, times(1)).getAuthentication(any(String.class));
            verify(refreshTokenRepository, times(1)).findById(any(Long.class));
            verify(jwtTokenProvider, times(1)).generateToken(any(Authentication.class));
            verify(memberService, times(1)).getMember(any(long.class));
        }

        @Test
        @DisplayName("유효하지 않은 refresh 토큰이라면, 토큰을 재발급 할 수 없다.")
        void invalidToken_willFail() {
            // given
            RefreshRequestDto refreshRequestDto = RefreshRequestDto.builder()
                .accessToken(
                    "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTcwMDU4NjkyOH0.lof7WjalCH1gGPy2q7YYi9VTcgn_aoFMwEMQvITtddsUIcJN-YzNODt_RQde5J5dH98NKMXDOvy7YwNlt6BCfg")
                .refreshToken(
                    "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MDExODk5Mjh9.uZuIAxsnf4Ubz5K9YzysJTu9Gh25XNTsPVAPSElw1lS78gS8S08L97Z4RkfGodegGXZ9UFFNkVXdhRzF9Pr-uA")
                .build();

            given(jwtTokenProvider.validateToken(any(String.class))).willReturn(false);

            // when
            Throwable exception = assertThrows(InvalidRefreshTokenException.class, () -> {
                authService.refresh(refreshRequestDto);
            });

            // then
            assertEquals("Refresh Token 이 유효하지 않습니다.", exception.getMessage());

            verify(jwtTokenProvider, times(1)).validateToken(any(String.class));
        }

        @Test
        @DisplayName("DB에 있는 Refresh 토큰과 일치하지 않는다면, 토큰을 재발급 할 수 없다.")
        void unmatchedMember_willFail() {
            // given
            RefreshRequestDto refreshRequestDto = RefreshRequestDto.builder()
                .accessToken(
                    "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTcwMDU4NjkyOH0.lof7WjalCH1gGPy2q7YYi9VTcgn_aoFMwEMQvITtddsUIcJN-YzNODt_RQde5J5dH98NKMXDOvy7YwNlt6BCfg")
                .refreshToken(
                    "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MDExODk5Mjh9.uZuIAxsnf4Ubz5K9YzysJTu9Gh25XNTsPVAPSElw1lS78gS8S08L97Z4RkfGodegGXZ9UFFNkVXdhRzF9Pr-uA")
                .build();
            Member member = Member.builder()
                .id(1L)
                .email("test@mail.com")
                .name("test")
                .password("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm")
                .photoUrl(
                    "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI")
                .authority(Authority.ROLE_USER)
                .build();
            UserDetails principal = new User(String.valueOf(member.getId()), "",
                Arrays.stream(new String[]{member.getAuthority().name()})
                    .map(SimpleGrantedAuthority::new)
                    .toList());
            Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "",
                Arrays.stream(new String[]{member.getAuthority().name()})
                    .map(SimpleGrantedAuthority::new)
                    .toList());
            RefreshToken refreshToken = RefreshToken.builder()
                .id(1L)
                .token(
                    "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MDExODk5Mjh9.uZuIAxsnf4Ubz5K9YzysJTu9Gh25XNTsPVAPSElw1lS78gS8S08L97Z4RkfGodegGXZ9UFFNkVXdhRzF9Pr-u")
                .build();

            given(jwtTokenProvider.validateToken(any(String.class))).willReturn(true);
            given(jwtTokenProvider.getAuthentication(any(String.class))).willReturn(authentication);
            given(refreshTokenRepository.findById(any(Long.class))).willReturn(
                Optional.of(refreshToken));

            // when
            Throwable exception = assertThrows(UnmatchedMemberException.class, () -> {
                authService.refresh(refreshRequestDto);
            });

            // then
            assertEquals("토큰의 회원 정보가 일치하지 않습니다.", exception.getMessage());

            verify(jwtTokenProvider, times(1)).validateToken(any(String.class));
            verify(jwtTokenProvider, times(1)).getAuthentication(any(String.class));
            verify(refreshTokenRepository, times(1)).findById(any(Long.class));
        }
    }
}
