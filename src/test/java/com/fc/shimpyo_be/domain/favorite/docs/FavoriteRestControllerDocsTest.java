package com.fc.shimpyo_be.domain.favorite.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fc.shimpyo_be.config.RestDocsSupport;
import com.fc.shimpyo_be.domain.favorite.dto.FavoriteResponseDto;
import com.fc.shimpyo_be.domain.favorite.dto.FavoritesResponseDto;
import com.fc.shimpyo_be.domain.favorite.service.FavoriteService;
import com.fc.shimpyo_be.domain.product.dto.response.ProductResponse;
import com.fc.shimpyo_be.domain.product.entity.Category;
import com.fc.shimpyo_be.global.util.SecurityUtil;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

public class FavoriteRestControllerDocsTest extends RestDocsSupport {

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    SecurityUtil securityUtil;

    @Test
    @DisplayName("register()은 즐겨찾기를 등록할 수 있다.")
    @WithMockUser(roles = "USER")
    void register() throws Exception {
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
            .andDo(restDoc.document(
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.favoriteId").type(JsonFieldType.NUMBER)
                        .description("즐겨찾기 식별자"),
                    fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                    fieldWithPath("data.productId").type(JsonFieldType.NUMBER).description("숙소 식별자")
                ))
            );
    }


    @Test
    @DisplayName("getFavorites()은 즐겨찾기 목록을 조회할 수 있다.")
    @WithMockUser(roles = "USER")
    void getFavorites() throws Exception {
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
            .andDo(restDoc.document(
                queryParameters(parameterWithName("page").optional().description("페이지 인덱스"),
                    parameterWithName("size").optional().description("페이지 사이즈")
                ),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.pageCount").type(JsonFieldType.NUMBER)
                        .description("총 페이지 개수"),
                    fieldWithPath("data.products").type(JsonFieldType.ARRAY)
                        .description("숙소 응답 데이터 배열"),
                    fieldWithPath("data.products[].productId").type(
                            JsonFieldType.NUMBER)
                        .description("숙소 아이디"),
                    fieldWithPath("data.products[].category").type(JsonFieldType.STRING)
                        .description("숙소 카테고리(호텔, 모텔, 풀빌라, 펜션)"),
                    fieldWithPath("data.products[].address").type(JsonFieldType.STRING)
                        .description("숙소 주소"),
                    fieldWithPath("data.products[].productName").type(
                            JsonFieldType.STRING)
                        .description("숙소 이름"),
                    fieldWithPath("data.products[].favorites").type(
                            JsonFieldType.BOOLEAN)
                        .description("즐겨찾기"),
                    fieldWithPath("data.products[].starAvg").type(JsonFieldType.NUMBER)
                        .description("숙소 평점"),
                    fieldWithPath("data.products[].image").type(JsonFieldType.STRING)
                        .description("숙소 썸네일 이미지"),
                    fieldWithPath("data.products[].price").type(JsonFieldType.NUMBER)
                        .description("숙소 내 방 최저 가격"),
                    fieldWithPath("data.products[].capacity").type(JsonFieldType.NUMBER)
                        .description("최대 인원")
                ))
            );
    }

    @Test
    @DisplayName("cancel은 즐겨찾기를 취소할 수 있다.")
    @WithMockUser(roles = "USER")
    void cancel() throws Exception {
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
            .andDo(restDoc.document(
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.favoriteId").type(JsonFieldType.NUMBER)
                        .description("즐겨찾기 식별자"),
                    fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                    fieldWithPath("data.productId").type(JsonFieldType.NUMBER).description("숙소 식별자")
                ))
            );
    }
}
