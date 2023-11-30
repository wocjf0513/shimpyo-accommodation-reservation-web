package com.fc.shimpyo_be.domain.member.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.shimpyo_be.config.AbstractContainersSupport;
import com.fc.shimpyo_be.domain.member.dto.request.CheckPasswordRequestDto;
import com.fc.shimpyo_be.domain.member.dto.request.UpdateMemberRequestDto;
import com.fc.shimpyo_be.domain.member.dto.response.MemberResponseDto;
import com.fc.shimpyo_be.domain.member.service.MemberService;
import com.fc.shimpyo_be.global.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberRestControllerTest extends AbstractContainersSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @MockBean
    MemberService memberService;

    @MockBean
    SecurityUtil securityUtil;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(this.context)
            .apply(springSecurity())
            .build();
    }

    @Nested
    @DisplayName("getMember()은")
    class Context_getMember {

        @Test
        @DisplayName("회원 정보를 조회할 수 있다.")
        @WithMockUser(roles = "USER")
        void _willSuccess() throws Exception {
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.memberId").isNumber())
                .andExpect(jsonPath("$.data.email").isString())
                .andExpect(jsonPath("$.data.name").isString())
                .andExpect(jsonPath("$.data.photoUrl").isString())
                .andDo(print());
        }
    }

    @Nested
    @DisplayName("updateMember()은")
    class Context_updateMember {

        @Test
        @DisplayName("회원 정보를 수정할 수 있다.")
        @WithMockUser(roles = "USER")
        void _willSuccess() throws Exception {
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.memberId").isNumber())
                .andExpect(jsonPath("$.data.email").isString())
                .andExpect(jsonPath("$.data.name").isString())
                .andExpect(jsonPath("$.data.photoUrl").isString())
                .andDo(print());
        }
    }

    @Nested
    @DisplayName("checkPassword()은")
    class Context_checkPassword {

        @Test
        @DisplayName("비밀번호가 일치하는지 확인할 수 있다.")
        @WithMockUser(roles = "USER")
        void _willSuccess() throws Exception {
            // given
            CheckPasswordRequestDto request = CheckPasswordRequestDto.builder()
                .password("qwer1234$$")
                .build();

            given(securityUtil.getCurrentMemberId()).willReturn(1L);
            doNothing().when(memberService).checkPassword(any(CheckPasswordRequestDto.class));

            // when then
            mockMvc.perform(post("/api/members")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());
        }
    }
}
