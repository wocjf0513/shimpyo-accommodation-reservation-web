package com.fc.shimpyo_be.domain.favorite.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fc.shimpyo_be.config.AbstractContainersSupport;
import com.fc.shimpyo_be.domain.favorite.dto.FavoriteResponseDto;
import com.fc.shimpyo_be.domain.favorite.dto.FavoritesResponseDto;
import com.fc.shimpyo_be.domain.favorite.service.FavoriteService;
import com.fc.shimpyo_be.domain.product.dto.response.ProductResponse;
import com.fc.shimpyo_be.domain.product.entity.Category;
import com.fc.shimpyo_be.global.util.SecurityUtil;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
public class FavoriteRestControllerTest extends AbstractContainersSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @MockBean
    FavoriteService favoriteService;

    @MockBean
    SecurityUtil securityUtil;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(this.context)
            .apply(springSecurity())
            .build();
    }

    @Nested
    @DisplayName("register()은")
    class Context_register {

        @Test
        @DisplayName("즐겨찾기를 등록할 수 있다.")
        @WithMockUser(roles = "USER")
        void _willSuccess() throws Exception {
            // given
            FavoriteResponseDto favoriteResponseDto = FavoriteResponseDto.builder()
                .favoriteId(1L)
                .memberId(1L)
                .productId(1L)
                .build();

            given(securityUtil.getCurrentMemberId()).willReturn(1L);
            given(favoriteService.register(any(Long.TYPE), any(Long.TYPE)))
                .willReturn(favoriteResponseDto);

            // when then
            mockMvc.perform(post("/api/favorites/{productId}", 1L))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.favoriteId").isNumber())
                .andExpect(jsonPath("$.data.memberId").isNumber())
                .andExpect(jsonPath("$.data.productId").isNumber())
                .andDo(print());
        }
    }

    @Nested
    @DisplayName("getFavorites()은")
    class Context_getFavorites {

        @Test
        @DisplayName("즐겨찾기 목록을 조회할 수 있다.")
        @WithMockUser(roles = "USER")
        void _willSuccess() throws Exception {
            // given
            FavoritesResponseDto favoritesResponseDto = FavoritesResponseDto.builder()
                .pageCount(10)
                .products(List.of(ProductResponse.builder()
                    .productId(1L)
                    .productName("OO 호텔")
                    .category(Category.TOURIST_HOTEL.getName())
                    .address("서울시 강남구 OO로 000-000 상세주소")
                    .favorites(true)
                    .image(
                        "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI")
                    .starAvg(5F)
                    .price(95000L)
                    .capacity(4L)
                    .build()))
                .build();

            given(securityUtil.getCurrentMemberId()).willReturn(1L);
            given(favoriteService.getFavorites(any(Long.TYPE), any(Pageable.class)))
                .willReturn(favoritesResponseDto);

            // when then
            mockMvc.perform(get("/api/favorites")
                    .queryParam("page", "0")
                    .queryParam("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.pageCount").isNumber())
                .andExpect(jsonPath("$.data.products[0].productId").isNumber())
                .andExpect(jsonPath("$.data.products[0].productName").isString())
                .andExpect(jsonPath("$.data.products[0].category").isString())
                .andExpect(jsonPath("$.data.products[0].address").isString())
                .andExpect(jsonPath("$.data.products[0].favorites").isBoolean())
                .andExpect(jsonPath("$.data.products[0].image").isString())
                .andExpect(jsonPath("$.data.products[0].starAvg").isNumber())
                .andExpect(jsonPath("$.data.products[0].price").isNumber())
                .andExpect(jsonPath("$.data.products[0].capacity").isNumber())
                .andDo(print());
        }
    }

    @Nested
    @DisplayName("cancel()은")
    class Context_cancel {

        @Test
        @DisplayName("즐겨찾기를 취소할 수 있다.")
        @WithMockUser(roles = "USER")
        void _willSuccess() throws Exception {
            // given
            FavoriteResponseDto favoriteResponseDto = FavoriteResponseDto.builder()
                .favoriteId(1L)
                .memberId(1L)
                .productId(1L)
                .build();

            given(securityUtil.getCurrentMemberId()).willReturn(1L);
            given(favoriteService.delete(any(Long.TYPE), any(Long.TYPE)))
                .willReturn(favoriteResponseDto);

            // when then
            mockMvc.perform(delete("/api/favorites/{productId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.favoriteId").isNumber())
                .andExpect(jsonPath("$.data.memberId").isNumber())
                .andExpect(jsonPath("$.data.productId").isNumber())
                .andDo(print());
        }
    }
}
