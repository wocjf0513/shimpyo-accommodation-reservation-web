package com.fc.shimpyo_be.domain.member.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fc.shimpyo_be.config.RestDocsSupport;
import com.fc.shimpyo_be.domain.member.dto.request.RefreshRequestDto;
import com.fc.shimpyo_be.domain.member.dto.request.SignInRequestDto;
import com.fc.shimpyo_be.domain.member.dto.request.SignUpRequestDto;
import com.fc.shimpyo_be.domain.member.dto.response.MemberResponseDto;
import com.fc.shimpyo_be.domain.member.dto.response.SignInResponseDto;
import com.fc.shimpyo_be.domain.member.dto.response.TokenResponseDto;
import com.fc.shimpyo_be.domain.member.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.JsonFieldType;

public class AuthRestControllerDocsTest extends RestDocsSupport {

    @MockBean
    private AuthService authService;

    private final ConstraintDescriptions signUpDescriptions = new ConstraintDescriptions(
        SignUpRequestDto.class);
    private final ConstraintDescriptions signInDescriptions = new ConstraintDescriptions(
        SignUpRequestDto.class);
    private final ConstraintDescriptions refreshDescriptions = new ConstraintDescriptions(
        SignUpRequestDto.class);

    @Test
    @DisplayName("signUp()은 회원 가입 할 수 있다.")
    void signUp() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
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

        // when
        mockMvc.perform(post("/api/auth/signup")
                .content(objectMapper.writeValueAsString(signUpRequestDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(restDoc.document(
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                        .attributes(key("constraints").value(
                            signUpDescriptions.descriptionsForProperty("email"))),
                    fieldWithPath("name").type(JsonFieldType.STRING).description("이름")
                        .attributes(key("constraints").value(
                            signUpDescriptions.descriptionsForProperty("name"))),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                        .attributes(key("constraints").value(
                            signUpDescriptions.descriptionsForProperty("password"))),
                    fieldWithPath("passwordConfirm").type(JsonFieldType.STRING)
                        .description("비밀번호 확인").attributes(key("constraints").value(
                            signUpDescriptions.descriptionsForProperty("passwordConfirm")))),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                    fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                    fieldWithPath("data.name").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("data.photoUrl").type(JsonFieldType.STRING)
                        .description("프로필 이미지")
                )
            ));

        verify(authService, times(1)).signUp(any(SignUpRequestDto.class));
    }

    @Test
    @DisplayName("signIn()은 회원 가입 할 수 있다.")
    void signIn() throws Exception {
        // given
        SignInRequestDto signInRequestDto = SignInRequestDto.builder()
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

        // when
        mockMvc.perform(post("/api/auth/signin")
                .content(objectMapper.writeValueAsString(signInRequestDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(restDoc.document(
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                        .attributes(key("constraints").value(
                            signInDescriptions.descriptionsForProperty("email"))),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                        .attributes(key("constraints").value(
                            signInDescriptions.descriptionsForProperty("password")))),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.member").type(JsonFieldType.OBJECT).description("회원 정보"),
                    fieldWithPath("data.member.memberId").type(JsonFieldType.NUMBER)
                        .description("회원 식별자"),
                    fieldWithPath("data.member.email").type(JsonFieldType.STRING)
                        .description("이메일"),
                    fieldWithPath("data.member.name").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("data.member.photoUrl").type(JsonFieldType.STRING)
                        .description("프로필 이미지"),
                    fieldWithPath("data.token").type(JsonFieldType.OBJECT).description("토큰 정보"),
                    fieldWithPath("data.token.grantType").type(JsonFieldType.STRING)
                        .description("권한 부여 유형"),
                    fieldWithPath("data.token.accessToken").type(JsonFieldType.STRING)
                        .description("Access Token"),
                    fieldWithPath("data.token.accessTokenExpiresIn").type(JsonFieldType.NUMBER)
                        .description("Access Token 만료 날짜"),
                    fieldWithPath("data.token.refreshToken").type(JsonFieldType.STRING)
                        .description("Refresh Token")
                )
            ));

        verify(authService, times(1)).signIn(any(SignInRequestDto.class));
    }

    @Test
    @DisplayName("refresh()는 토큰을 재발급 할 수 있다.")
    void refresh() throws Exception {
        // given
        RefreshRequestDto refreshRequestDto = RefreshRequestDto.builder()
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

        // when
        mockMvc.perform(post("/api/auth/refresh")
                .content(objectMapper.writeValueAsString(refreshRequestDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(restDoc.document(
                requestFields(
                    fieldWithPath("accessToken").type(JsonFieldType.STRING)
                        .description("Access Token")
                        .attributes(key("constraints").value(
                            refreshDescriptions.descriptionsForProperty("accessToken"))),
                    fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                        .description("Refresh Token")
                        .attributes(key("constraints").value(
                            refreshDescriptions.descriptionsForProperty("refreshToken")))),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.member").type(JsonFieldType.OBJECT).description("회원 정보"),
                    fieldWithPath("data.member.memberId").type(JsonFieldType.NUMBER)
                        .description("회원 식별자"),
                    fieldWithPath("data.member.email").type(JsonFieldType.STRING)
                        .description("이메일"),
                    fieldWithPath("data.member.name").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("data.member.photoUrl").type(JsonFieldType.STRING)
                        .description("프로필 이미지"),
                    fieldWithPath("data.token").type(JsonFieldType.OBJECT).description("토큰 정보"),
                    fieldWithPath("data.token.grantType").type(JsonFieldType.STRING)
                        .description("권한 부여 유형"),
                    fieldWithPath("data.token.accessToken").type(JsonFieldType.STRING)
                        .description("Access Token"),
                    fieldWithPath("data.token.accessTokenExpiresIn").type(JsonFieldType.NUMBER)
                        .description("Access Token 만료 날짜"),
                    fieldWithPath("data.token.refreshToken").type(JsonFieldType.STRING)
                        .description("Refresh Token")
                )
            ));

        verify(authService, times(1)).refresh(any(RefreshRequestDto.class));
    }
}
