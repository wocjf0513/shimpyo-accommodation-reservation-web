package com.fc.shimpyo_be.domain.member.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import com.fc.shimpyo_be.config.RestDocsSupport;
import com.fc.shimpyo_be.domain.member.dto.request.CheckPasswordRequestDto;
import com.fc.shimpyo_be.domain.member.dto.request.UpdateMemberRequestDto;
import com.fc.shimpyo_be.domain.member.dto.response.CheckPasswordResponseDto;
import com.fc.shimpyo_be.domain.member.dto.response.MemberResponseDto;
import com.fc.shimpyo_be.domain.member.service.MemberService;
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

    private final ConstraintDescriptions CheckPasswordDescriptions = new ConstraintDescriptions(
        CheckPasswordRequestDto.class);

    @Test
    @DisplayName("checkPassword()은 비밀번호가 일치하는지 확인할 수 있다.")
    @WithMockUser(roles = "USER")
    void checkPassword() throws Exception {
        // given
        CheckPasswordRequestDto request = CheckPasswordRequestDto.builder()
            .password("qwer1234$$")
            .build();
        CheckPasswordResponseDto checkPasswordResponseDto = CheckPasswordResponseDto.builder()
            .isCorrectPassword(true)
            .build();

        given(memberService.checkPassword(any(CheckPasswordRequestDto.class))).willReturn(
            checkPasswordResponseDto);

        // when then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/members")
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(restDoc.document(
                requestFields(
                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                        .attributes(key("constraints").value(
                            CheckPasswordDescriptions.descriptionsForProperty("password")))),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.correctPassword").type(JsonFieldType.BOOLEAN)
                        .description("회원 식별자")
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

        given(memberService.updateMember(any(UpdateMemberRequestDto.class))).willReturn(
            memberResponseDto);

        // when then
        mockMvc.perform(patch("/api/members")
                .with(csrf())
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
}
