package com.fc.shimpyo_be.domain.product.controller;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.shimpyo_be.domain.product.controller.factory.ProductFactory;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;


    @DisplayName("숙소 저장 후, 검색 조회 및 페이징할 수 있다.")
    @WithMockUser
    @Test
    void shouldSuccessToGetAllProducts() throws Exception {
        // given
        for (int i = 0;  i<5; i++) {
            productRepository.save(ProductFactory.createTestProduct());
        }

        // when
        ResultActions getProductAction = mockMvc.perform(
            get("/api/products?page=0&size=3"));

        // then
        getProductAction
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());
    }

}