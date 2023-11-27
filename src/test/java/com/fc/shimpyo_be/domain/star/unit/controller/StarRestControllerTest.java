package com.fc.shimpyo_be.domain.star.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.shimpyo_be.domain.star.dto.request.StarRegisterRequestDto;
import com.fc.shimpyo_be.domain.star.dto.response.StarResponseDto;
import com.fc.shimpyo_be.domain.star.service.StarService;
import com.fc.shimpyo_be.global.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class StarRestControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private StarService starService;

    @MockBean
    private SecurityUtil securityUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(@Autowired WebApplicationContext applicationContext) {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(applicationContext)
            .apply(springSecurity())
            .alwaysDo(print())
            .build();
    }

    @WithMockUser(roles = "USER")
    @DisplayName("[api][POST] 별점 등록 API 성공 테스트")
    @Test
    void register_success_test() throws Exception {
        // given
        String requestUrl = "/api/stars";
        Long productId = 1L;
        float score = 3.5F;

        StarRegisterRequestDto requestDto
            = new StarRegisterRequestDto(productId, score);
        Long registeredStarId = 1L;
        StarResponseDto responseDto = new StarResponseDto(registeredStarId, score);

        given(securityUtil.getCurrentMemberId()).willReturn(1L);
        given(starService.register(anyLong(), any(StarRegisterRequestDto.class)))
            .willReturn(responseDto);

        // when & then
        mockMvc.perform(
                post(requestUrl)
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.starId").isNumber())
            .andExpect(jsonPath("$.data.score").isNotEmpty());
    }
}
