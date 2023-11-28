package com.fc.shimpyo_be.domain.member.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.shimpyo_be.config.AbstractContainersSupport;
import com.fc.shimpyo_be.domain.member.dto.request.RefreshRequestDto;
import com.fc.shimpyo_be.domain.member.dto.request.SignInRequestDto;
import com.fc.shimpyo_be.domain.member.dto.request.SignUpRequestDto;
import com.fc.shimpyo_be.domain.member.dto.response.MemberResponseDto;
import com.fc.shimpyo_be.domain.member.dto.response.SignInResponseDto;
import com.fc.shimpyo_be.domain.member.dto.response.TokenResponseDto;
import com.fc.shimpyo_be.domain.member.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthRestControllerTest extends AbstractContainersSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @MockBean
    AuthService authService;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(this.context)
            .apply(springSecurity())
            .build();
    }

    @Nested
    @DisplayName("signUp()은")
    class Context_signUp {

        @Test
        @DisplayName("회원 가입 할 수 있다.")
        void _willSuccess() throws Exception {
            // given
            SignUpRequestDto request = SignUpRequestDto.builder()
                .email("test@mail.com")
                .name("test")
                .password("qwer1234$$")
                .passwordConfirm("qwer1234$$")
                .build();
            MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                .memberId(1L)
                .email("test@mail.com")
                .name("test")
                .photoUrl("")
                .build();

            given(authService.signUp(any(SignUpRequestDto.class))).willReturn(memberResponseDto);

            // when then
            mockMvc.perform(post("/api/auth/signup")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.memberId").isNumber())
                .andExpect(jsonPath("$.data.email").isString())
                .andExpect(jsonPath("$.data.name").isString())
                .andExpect(jsonPath("$.data.photoUrl").isString())
                .andDo(print());
            verify(authService, times(1)).signUp(any(SignUpRequestDto.class));
        }
    }

    @Nested
    @DisplayName("signIn()은")
    class Context_signIn {

        @Test
        @DisplayName("로그인 할 수 있다.")
        void _willSuccess() throws Exception {
            // given
            SignInRequestDto request = SignInRequestDto.builder()
                .email("test@mail.com")
                .password("qwer1234$$")
                .build();
            MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                .memberId(1L)
                .email("test@mail.com")
                .name("test")
                .photoUrl("")
                .build();
            TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .grantType("Bearer")
                .accessToken(
                    "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTcwMDU4NjkyOH0.lof7WjalCH1gGPy2q7YYi9VTcgn_aoFMwEMQvITtddsUIcJN-YzNODt_RQde5J5dH98NKMXDOvy7YwNlt6BCfg")
                .accessTokenExpiresIn(1700586928520L)
                .refreshToken(
                    "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MDExODk5Mjh9.uZuIAxsnf4Ubz5K9YzysJTu9Gh25XNTsPVAPSElw1lS78gS8S08L97Z4RkfGodegGXZ9UFFNkVXdhRzF9Pr-uA")
                .build();
            SignInResponseDto signInResponseDto = SignInResponseDto.builder()
                .member(memberResponseDto)
                .token(tokenResponseDto)
                .build();

            given(authService.signIn(any(SignInRequestDto.class))).willReturn(signInResponseDto);

            // when then
            mockMvc.perform(post("/api/auth/signin")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.member").isMap())
                .andExpect(jsonPath("$.data.member.memberId").isNumber())
                .andExpect(jsonPath("$.data.member.email").isString())
                .andExpect(jsonPath("$.data.member.name").isString())
                .andExpect(jsonPath("$.data.member.photoUrl").isString())
                .andExpect(jsonPath("$.data.token").isMap())
                .andExpect(jsonPath("$.data.token.grantType").isString())
                .andExpect(jsonPath("$.data.token.accessToken").isString())
                .andExpect(jsonPath("$.data.token.accessTokenExpiresIn").isNumber())
                .andExpect(jsonPath("$.data.token.refreshToken").isString())
                .andDo(print());
            verify(authService, times(1)).signIn(any(SignInRequestDto.class));
        }
    }

    @Nested
    @DisplayName("refresh()은")
    class Context_refresh {

        @Test
        @DisplayName("토큰을 재발급 할 수 있다.")
        void _willSuccess() throws Exception {
            // given
            RefreshRequestDto request = RefreshRequestDto.builder()
                .accessToken(
                    "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTcwMDU4NjkyOH0.lof7WjalCH1gGPy2q7YYi9VTcgn_aoFMwEMQvITtddsUIcJN-YzNODt_RQde5J5dH98NKMXDOvy7YwNlt6BCfg")
                .refreshToken(
                    "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MDExODk5Mjh9.uZuIAxsnf4Ubz5K9YzysJTu9Gh25XNTsPVAPSElw1lS78gS8S08L97Z4RkfGodegGXZ9UFFNkVXdhRzF9Pr-uA")
                .build();
            MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                .memberId(1L)
                .email("test@mail.com")
                .name("test")
                .photoUrl("")
                .build();
            TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .grantType("Bearer")
                .accessToken(
                    "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTcwMDU4NjkyOH0.lof7WjalCH1gGPy2q7YYi9VTcgn_aoFMwEMQvITtddsUIcJN-YzNODt_RQde5J5dH98NKMXDOvy7YwNlt6BCfg")
                .accessTokenExpiresIn(1700586928520L)
                .refreshToken(
                    "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MDExODk5Mjh9.uZuIAxsnf4Ubz5K9YzysJTu9Gh25XNTsPVAPSElw1lS78gS8S08L97Z4RkfGodegGXZ9UFFNkVXdhRzF9Pr-uA")
                .build();
            SignInResponseDto signInResponseDto = SignInResponseDto.builder()
                .member(memberResponseDto)
                .token(tokenResponseDto)
                .build();

            given(authService.refresh(any(RefreshRequestDto.class))).willReturn(signInResponseDto);

            // when then
            mockMvc.perform(post("/api/auth/refresh")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.member").isMap())
                .andExpect(jsonPath("$.data.member.memberId").isNumber())
                .andExpect(jsonPath("$.data.member.email").isString())
                .andExpect(jsonPath("$.data.member.name").isString())
                .andExpect(jsonPath("$.data.member.photoUrl").isString())
                .andExpect(jsonPath("$.data.token").isMap())
                .andExpect(jsonPath("$.data.token.grantType").isString())
                .andExpect(jsonPath("$.data.token.accessToken").isString())
                .andExpect(jsonPath("$.data.token.accessTokenExpiresIn").isNumber())
                .andExpect(jsonPath("$.data.token.refreshToken").isString())
                .andDo(print());
            verify(authService, times(1)).refresh(any(RefreshRequestDto.class));
        }
    }
}
