package com.fc.shimpyo_be.domain.member.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import com.fc.shimpyo_be.config.RestDocsSupport;
import com.fc.shimpyo_be.domain.member.dto.request.CheckPasswordRequestDto;
import com.fc.shimpyo_be.domain.member.dto.request.UpdateMemberRequestDto;
import com.fc.shimpyo_be.domain.member.dto.response.MemberResponseDto;
import com.fc.shimpyo_be.domain.member.service.MemberService;
import com.fc.shimpyo_be.global.util.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class MemberRestControllerDocsTest extends RestDocsSupport {

    @MockBean
    private MemberService memberService;

    @MockBean
    SecurityUtil securityUtil;

    private final ConstraintDescriptions CheckPasswordDescriptions = new ConstraintDescriptions(
        CheckPasswordRequestDto.class);

    @Test
    @DisplayName("getMember()은 회원 정보를 조회할 수 있다.")
    @WithMockUser(roles = "USER")
    void getMember() throws Exception {
        // given
        MemberResponseDto memberResponseDto = MemberResponseDto.builder()
            .memberId(1L)
            .email("test@mail.com")
            .name("test")
            .photoUrl(
                "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI")
            .build();

        given(securityUtil.getCurrentMemberId()).willReturn(1L);
        given(memberService.getMember()).willReturn(memberResponseDto);

        // when then
        mockMvc.perform(get("/api/members"))
            .andDo(restDoc.document(
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                    fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                    fieldWithPath("data.name").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("data.photoUrl").type(JsonFieldType.STRING)
                        .description("프로필 이미지")
                ))
            );
    }

    @Test
    @DisplayName("updateMember()은 회원 정보를 수정할 수 있다.")
    @WithMockUser(roles = "USER")
    void updateMember() throws Exception {
        // given
        UpdateMemberRequestDto request = UpdateMemberRequestDto.builder()
            .photoUrl(
                "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI")
            .build();
        MemberResponseDto memberResponseDto = MemberResponseDto.builder()
            .memberId(1L)
            .email("test@mail.com")
            .name("test")
            .photoUrl(
                "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI")
            .build();

        given(securityUtil.getCurrentMemberId()).willReturn(1L);
        given(memberService.updateMember(any(UpdateMemberRequestDto.class))).willReturn(
            memberResponseDto);

        // when then
        mockMvc.perform(patch("/api/members")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(restDoc.document(
                requestFields(
                    fieldWithPath("password").type(JsonFieldType.STRING).optional()
                        .description("비밀번호"),
                    fieldWithPath("passwordConfirm").type(JsonFieldType.STRING).optional()
                        .description("비밀번호 확인"),
                    fieldWithPath("photoUrl").type(JsonFieldType.STRING).optional()
                        .description("프로필 이미지")),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                    fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                    fieldWithPath("data.name").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("data.photoUrl").type(JsonFieldType.STRING)
                        .description("프로필 이미지")
                ))
            );
    }

    @Test
    @DisplayName("checkPassword()은 비밀번호가 일치하는지 확인할 수 있다.")
    @WithMockUser(roles = "USER")
    void checkPassword() throws Exception {
        // given
        CheckPasswordRequestDto request = CheckPasswordRequestDto.builder()
            .password("qwer1234$$")
            .build();

        given(securityUtil.getCurrentMemberId()).willReturn(1L);
        doNothing().when(memberService).checkPassword(any(CheckPasswordRequestDto.class));

        // when then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/members")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(restDoc.document(
                requestFields(
                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                        .attributes(key("constraints").value(
                            CheckPasswordDescriptions.descriptionsForProperty("password")))),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터")
                ))
            );
    }
}
