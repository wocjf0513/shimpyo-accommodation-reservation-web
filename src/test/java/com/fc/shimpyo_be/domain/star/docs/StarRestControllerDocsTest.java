package com.fc.shimpyo_be.domain.star.docs;

import com.fc.shimpyo_be.config.RestDocsSupport;
import com.fc.shimpyo_be.domain.star.dto.request.StarRegisterRequestDto;
import com.fc.shimpyo_be.domain.star.dto.response.StarResponseDto;
import com.fc.shimpyo_be.domain.star.service.StarService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StarRestControllerDocsTest extends RestDocsSupport {

    @MockBean
    private StarService starService;

    private final ConstraintDescriptions starRegisterDescriptions
        = new ConstraintDescriptions(StarRegisterRequestDto.class);

    @WithMockUser(roles = "USER")
    @Test
    @DisplayName("register()은 별점을 등록 할 수 있다.")
    void register() throws Exception {
        // given
        String requestUrl = "/api/stars";
        Long memberId = 1L;
        Long productId = 1L;
        float score = 3.5F;

        StarRegisterRequestDto requestDto = new StarRegisterRequestDto(memberId, productId, score);
        StarResponseDto responseDto = new StarResponseDto(1L, score);

        given(starService.register(any(StarRegisterRequestDto.class)))
            .willReturn(responseDto);

        // when
        mockMvc.perform(post(requestUrl)
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(restDoc.document(
                    requestFields(
                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("별점 등록하는 회원 아이디")
                            .attributes(key("constraints").value(
                                starRegisterDescriptions.descriptionsForProperty("memberId"))),
                        fieldWithPath("productId").type(JsonFieldType.NUMBER).description("별점 등록 대상 숙소 아이디")
                            .attributes(key("constraints").value(
                                starRegisterDescriptions.descriptionsForProperty("productId"))),
                        fieldWithPath("score").type(JsonFieldType.NUMBER).description("별점 점수")
                            .attributes(key("constraints").value(
                                starRegisterDescriptions.descriptionsForProperty("score")))
                    ),
                    responseFields(responseCommon()).and(
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.starId").type(JsonFieldType.NUMBER).description("등록된 별점 식별자"),
                        fieldWithPath("data.score").type(JsonFieldType.NUMBER).description("별점 점수")
                    )
                )
            );

        verify(starService, times(1)).register(any(StarRegisterRequestDto.class));
    }
}
