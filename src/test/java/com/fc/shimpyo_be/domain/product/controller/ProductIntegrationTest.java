package com.fc.shimpyo_be.domain.product.controller;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fc.shimpyo_be.config.RestDocsSupport;
import com.fc.shimpyo_be.domain.product.dto.response.ProductDetailsResponse;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.entity.ProductImage;
import com.fc.shimpyo_be.domain.product.factory.ProductFactory;
import com.fc.shimpyo_be.domain.product.repository.ProductImageRepository;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.product.util.ProductMapper;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


@AutoConfigureMockMvc
class ProductIntegrationTest extends RestDocsSupport {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ProductImageRepository productImageRepository;


    private void assertProductDetailsResponse(Product expectedProduct, ResultActions resultActions)
        throws Exception {
        // then
        ProductDetailsResponse expectedProductDetailsResponse = ProductMapper.toProductDetailsResponse(
            expectedProduct);

        resultActions.andDo(MockMvcResultHandlers.print()).andExpect(status().is2xxSuccessful())
            .andExpect(
                jsonPath("$.data.productId").value(expectedProductDetailsResponse.productId()))
            .andExpect(
                jsonPath("$.data.productName").value(expectedProductDetailsResponse.productName()))
            .andExpect(jsonPath("$.data.address").value(expectedProductDetailsResponse.address()))
            .andExpect(jsonPath("$.data.category").value(expectedProductDetailsResponse.category()))
            .andExpect(jsonPath("$.data.starAvg").value(expectedProductDetailsResponse.starAvg()));


    }


    @DisplayName("숙소 저장 후, 검색 조회 및 페이징할 수 있다.")
    @Test
    @WithMockUser
    void shouldSuccessToGetAllProducts() throws Exception {
        // given
        for (int i = 0; i < 20; i++) {
            Product product = productRepository.save(ProductFactory.createTestProduct());
            ProductImage productImage = productImageRepository.save(
                ProductFactory.createTestProductImage(product));
            Room room = roomRepository.save(ProductFactory.createTestRoom(product));
            product.getRooms().add(room);
        }

        // when
        ResultActions getProductAction = mockMvc.perform(
            get("/api/products?page=0&size=20&address=서울시&category=호텔,모텔"));

        // then
        getProductAction.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk()).andDo(
            restDoc.document(
                queryParameters(parameterWithName("page").optional().description("페이지 인덱스"),
                    parameterWithName("size").optional().description("페이지 사이즈"),
                    parameterWithName("sort").optional().description("정렬 할 컬럼 및 방향"),
                    parameterWithName("address").optional().description("상품 주소"),
                    parameterWithName("category").optional().description("상품 카테고리"),
                    parameterWithName("productName").optional().description("상품 이름")),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
                    fieldWithPath("data[].productId").type(JsonFieldType.NUMBER)
                        .description("상품 아이디"),
                    fieldWithPath("data[].category").type(JsonFieldType.STRING)
                        .description("상품 카테고리(호텔, 모텔, 풀빌라, 펜션)"),
                    fieldWithPath("data[].address").type(JsonFieldType.STRING).description("상품 주소"),
                    fieldWithPath("data[].productName").type(JsonFieldType.STRING)
                        .description("상품 이름"),
                    fieldWithPath("data[].favorites").type(JsonFieldType.BOOLEAN)
                        .description("즐겨찾기"),
                    fieldWithPath("data[].starAvg").type(JsonFieldType.NUMBER).description("상품 평점"),
                    fieldWithPath("data[].image").type(JsonFieldType.STRING)
                        .description("상품 썸네일 이미지"),
                    fieldWithPath("data[].price").type(JsonFieldType.NUMBER)
                        .description("상품 내 방 최저 가격"))));

    }

    @DisplayName("숙소 상세 검색을 할 수 있다.")
    @Test
    @WithMockUser
    void shouldSuccessToGetDetailsProduct() throws Exception {
        // given
//        Member member = memberRepository.save(ProductFactory.createTestMember());
        Product product = productRepository.save(ProductFactory.createTestProduct());
        ProductImage productImage = productImageRepository.save(
            ProductFactory.createTestProductImage(product));
        ProductImage productImage2 = productImageRepository.save(
            ProductFactory.createTestProductImage(product));

//        Reservation reservation = reservationRepository.save(
//            ProductFactory.createTestReservation(member));

        for (int i = 0; i < 5; i++) {
            Room room = roomRepository.save(ProductFactory.createTestRoom(product));
            product.getRooms().add(room);
//            if(i%2==0)
//            reservationProductRepository.save(ProductFactory.createTestReservationProduct(room, reservation));
        }

        // when
        ResultActions getProductAction = mockMvc.perform(
            get("/api/products/{productId}?startDate=2023-11-22&endDate=2023-11-23",
                product.getId()));

        // then
        assertProductDetailsResponse(product, getProductAction);
        getProductAction.andDo(
            restDoc.document(pathParameters(parameterWithName("productId").description("상품 아이디")),
                queryParameters(parameterWithName("startDate").description("상세 검색, 체크인 일"),
                    parameterWithName("endDate").description("상세 검색, 체크아웃 일")), responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.productId").type(JsonFieldType.NUMBER)
                        .description("상품 아이디"),
                    fieldWithPath("data.category").type(JsonFieldType.STRING)
                        .description("상품 카테고리(호텔, 모텔, 풀빌라, 펜션)"),
                    fieldWithPath("data.address").type(JsonFieldType.STRING).description("상품 주소"),
                    fieldWithPath("data.productName").type(JsonFieldType.STRING)
                        .description("상품 이름"),
                    fieldWithPath("data.description").type(JsonFieldType.STRING)
                        .description("상품 설명"),
                    fieldWithPath("data.favorites").type(JsonFieldType.BOOLEAN).description("즐겨찾기"),
                    fieldWithPath("data.starAvg").type(JsonFieldType.NUMBER).description("상품 평점"),
                    fieldWithPath("data.images").type(JsonFieldType.ARRAY).description("상품 관련 이미지"),
                    fieldWithPath("data.rooms").type(JsonFieldType.ARRAY)
                        .description("상품 하위 방 데이터"),
                    fieldWithPath("data.rooms[].roomId").type(JsonFieldType.NUMBER)
                        .description("방 아이디"),
                    fieldWithPath("data.rooms[].roomName").type(JsonFieldType.STRING)
                        .description("방 이름"),
                    fieldWithPath("data.rooms[].price").type(JsonFieldType.NUMBER)
                        .description("방 가격"),
                    fieldWithPath("data.rooms[].description").type(JsonFieldType.STRING)
                        .description("방 설명"),
                    fieldWithPath("data.rooms[].standard").type(JsonFieldType.NUMBER)
                        .description("기준 인원"),
                    fieldWithPath("data.rooms[].capacity").type(JsonFieldType.NUMBER)
                        .description("최대 인원"),
                    fieldWithPath("data.rooms[].checkIn").type(JsonFieldType.STRING)
                        .description("체크인 시간"),
                    fieldWithPath("data.rooms[].checkOut").type(JsonFieldType.STRING)
                        .description("체크아웃 시간"),
                    fieldWithPath("data.rooms[].reserved").type(JsonFieldType.BOOLEAN)
                        .description("예약 여부"))));

    }
//
//    @Test
//    @DisplayName("예약 가능 여부를 확인할 수 있다.")
//    @WithMockUser
//    void isAvailableForReservation() throws Exception {
//        // given
//        Product product = productRepository.save(ProductFactory.createTestProduct());
//        Room room = roomRepository.save(ProductFactory.createTestRoom(product));
//        product.getRooms().add(room);
//
//        // when
//        ResultActions getProductAction = mockMvc.perform(
//            get("/api/products/amounts/{roomId}?startDate=2023-11-22&endDate=2023-11-23",room.getId()));
//
//        // then
//        getProductAction
//            .andExpect(status().isOk())
//            .andDo(MockMvcResultHandlers.print())
//            .andDo(restDoc.document(
//                pathParameters(
//                    parameterWithName("roomId").description("방 아이디")
//                ),
//                queryParameters(
//                    parameterWithName("startDate").description("상세 검색, 체크인 일"),
//                    parameterWithName("endDate").description("상세 검색, 체크아웃 일")
//                ),
//                responseFields(
//                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
//                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
//                    fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터 NULL")
//                )
//            ));
//    }


}