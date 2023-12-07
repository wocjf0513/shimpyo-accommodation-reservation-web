package com.fc.shimpyo_be.domain.product.docs;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fc.shimpyo_be.config.RestDocsSupport;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.entity.ProductImage;
import com.fc.shimpyo_be.domain.product.factory.ProductFactory;
import com.fc.shimpyo_be.domain.product.repository.ProductImageRepository;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductRestIntegrationDocsTest extends RestDocsSupport {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private RedisTemplate<String, Object> restTemplate;

    @DisplayName("숙소 저장 후, 검색 조회 및 페이징할 수 있다.")
    @Test
    @WithMockUser
    void getProducts() throws Exception {
        // given
        for (int i = 0; i < 5; i++) {
            Product product = productRepository.save(ProductFactory.createTestProduct());
            ProductImage productImage = productImageRepository.save(
                ProductFactory.createTestProductImage(product));
            Room room = roomRepository.save(ProductFactory.createTestRoom(product));
            product.getRooms().add(room);
        }

        // when
        ResultActions getProductAction = mockMvc.perform(
            get("/api/products?page=0&size=20"));

        // then
        getProductAction.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk()).andDo(
            restDoc.document(
                queryParameters(parameterWithName("page").optional().description("페이지 인덱스"),
                    parameterWithName("size").optional().description("페이지 사이즈"),
                    parameterWithName("sort").optional().description("정렬 할 컬럼 및 방향"),
                    parameterWithName("address").optional().description("숙소 주소"),
                    parameterWithName("category").optional().description("숙소 카테고리"),
                    parameterWithName("productName").optional().description("숙소 이름")),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
                    fieldWithPath("data[].productId").type(JsonFieldType.NUMBER)
                        .description("숙소 아이디"),
                    fieldWithPath("data[].category").type(JsonFieldType.STRING)
                        .description("숙소 카테고리(호텔, 모텔, 풀빌라, 펜션)"),
                    fieldWithPath("data[].address").type(JsonFieldType.STRING).description("숙소 주소"),
                    fieldWithPath("data[].productName").type(JsonFieldType.STRING)
                        .description("숙소 이름"),
                    fieldWithPath("data[].favorites").type(JsonFieldType.BOOLEAN)
                        .description("즐겨찾기"),
                    fieldWithPath("data[].starAvg").type(JsonFieldType.NUMBER).description("숙소 평점"),
                    fieldWithPath("data[].image").type(JsonFieldType.STRING)
                        .description("숙소 썸네일 이미지"),
                    fieldWithPath("data[].price").type(JsonFieldType.NUMBER)
                        .description("숙소 내 방 최저 가격"),
                    fieldWithPath("data[].capacity").type(JsonFieldType.NUMBER)
                        .description("최대 인원"))));

    }

    @DisplayName("숙소 상세 검색을 할 수 있다.")
    @Test
    @WithMockUser
    void getProductDetails() throws Exception {
        // given
        Product product = productRepository.save(ProductFactory.createTestProduct());
        ProductImage productImage = productImageRepository.save(
            ProductFactory.createTestProductImage(product));
        ProductImage productImage2 = productImageRepository.save(
            ProductFactory.createTestProductImage(product));

        for (int i = 0; i < 5; i++) {
            Room room = roomRepository.save(ProductFactory.createTestRoom(product));
            product.getRooms().add(room);
        }

        // when
        ResultActions getProductAction = mockMvc.perform(
            get("/api/products/{productId}?startDate=2023-12-22&endDate=2023-12-23",
                product.getId()));

        // then
        getProductAction.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk()).andDo(
            restDoc.document(pathParameters(parameterWithName("productId").description("숙소 아이디")),
                queryParameters(parameterWithName("startDate").description("상세 검색, 체크인 일"),
                    parameterWithName("endDate").description("상세 검색, 체크아웃 일")), responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.productId").type(JsonFieldType.NUMBER)
                        .description("숙소 식별자"),
                    fieldWithPath("data.productAmenityResponse").type(JsonFieldType.OBJECT)
                        .description("숙소 어메니티"),
                    fieldWithPath("data.productAmenityResponse.barbecue").type(JsonFieldType.BOOLEAN)
                        .description("숙소 바베큐장 여부"),
                    fieldWithPath("data.productAmenityResponse.beauty").type(JsonFieldType.BOOLEAN)
                        .description("숙소 뷰티시설 여부"),
                    fieldWithPath("data.productAmenityResponse.beverage").type(JsonFieldType.BOOLEAN)
                        .description("숙소 식음료장 여부"),
                    fieldWithPath("data.productAmenityResponse.bicycle").type(JsonFieldType.BOOLEAN)
                        .description("숙소 자전거 대여 여부"),
                    fieldWithPath("data.productAmenityResponse.campfire").type(JsonFieldType.BOOLEAN)
                        .description("숙소 캠프파이어 여부"),
                    fieldWithPath("data.productAmenityResponse.fitness").type(JsonFieldType.BOOLEAN)
                        .description("숙소 휘트니스 센터 여부"),
                    fieldWithPath("data.productAmenityResponse.karaoke").type(JsonFieldType.BOOLEAN)
                        .description("숙소 노래방 여부"),
                    fieldWithPath("data.productAmenityResponse.publicBath").type(JsonFieldType.BOOLEAN)
                        .description("숙소 공동 샤워실 여부"),
                    fieldWithPath("data.productAmenityResponse.publicPc").type(JsonFieldType.BOOLEAN)
                        .description("숙소 공동 PC실 여부"),
                    fieldWithPath("data.productAmenityResponse.sauna").type(JsonFieldType.BOOLEAN)
                        .description("숙소 사우나 시설 여부"),
                    fieldWithPath("data.productAmenityResponse.sports").type(JsonFieldType.BOOLEAN)
                        .description("숙소 스포츠 시설 여부"),
                    fieldWithPath("data.productAmenityResponse.seminar").type(JsonFieldType.BOOLEAN)
                        .description("숙소 세미나실 여부"),
                    fieldWithPath("data.productOptionResponse").type(JsonFieldType.OBJECT)
                        .description("숙소 옵션"),
                    fieldWithPath("data.productOptionResponse.cooking").type(JsonFieldType.BOOLEAN)
                        .description("객실 내 취사 여부"),
                    fieldWithPath("data.productOptionResponse.parking").type(JsonFieldType.BOOLEAN)
                        .description("숙소 주차 시설 여부"),
                    fieldWithPath("data.productOptionResponse.pickup").type(JsonFieldType.BOOLEAN)
                        .description("숙소 픽업 서비스 여부"),
                    fieldWithPath("data.productOptionResponse.foodPlace").type(JsonFieldType.STRING)
                        .description("숙소 식음료장"),
                    fieldWithPath("data.productOptionResponse.infoCenter").type(JsonFieldType.STRING)
                        .description("숙소 문의 및 안내 번호"),
                    fieldWithPath("data.category").type(JsonFieldType.STRING)
                        .description("숙소 카테고리(호텔, 모텔, 풀빌라, 펜션)"),
                    fieldWithPath("data.address").type(JsonFieldType.STRING).description("숙소 주소"),
                    fieldWithPath("data.productName").type(JsonFieldType.STRING)
                        .description("숙소 이름"),
                    fieldWithPath("data.description").type(JsonFieldType.STRING)
                        .description("숙소 설명"),
                    fieldWithPath("data.favorites").type(JsonFieldType.BOOLEAN).description("즐겨찾기"),
                    fieldWithPath("data.starAvg").type(JsonFieldType.NUMBER).description("숙소 평점"),
                    fieldWithPath("data.images").type(JsonFieldType.ARRAY).description("숙소 관련 이미지"),
                    fieldWithPath("data.rooms").type(JsonFieldType.ARRAY)
                        .description("숙소 하위 방 데이터"),
                    fieldWithPath("data.rooms[].roomOptionResponse").type(JsonFieldType.OBJECT)
                        .description("방 옵션"),
                    fieldWithPath("data.rooms[].roomOptionResponse.bathFacility").type(JsonFieldType.BOOLEAN)
                        .description("객실 내 목욕 시설 여부"),
                    fieldWithPath("data.rooms[].roomOptionResponse.bath").type(JsonFieldType.BOOLEAN)
                        .description("객실 내 욕조 여부"),
                    fieldWithPath("data.rooms[].roomOptionResponse.homeTheater").type(JsonFieldType.BOOLEAN)
                        .description("객실 내 홈시어터 여부"),
                    fieldWithPath("data.rooms[].roomOptionResponse.airCondition").type(JsonFieldType.BOOLEAN)
                        .description("객실 내 에어컨 여부"),
                    fieldWithPath("data.rooms[].roomOptionResponse.tv").type(JsonFieldType.BOOLEAN)
                        .description("객실 내 TV 여부"),
                    fieldWithPath("data.rooms[].roomOptionResponse.pc").type(JsonFieldType.BOOLEAN)
                        .description("객실 내 PC 여부"),
                    fieldWithPath("data.rooms[].roomOptionResponse.cable").type(JsonFieldType.BOOLEAN)
                        .description("객실 내 케이블 설치 여부"),
                    fieldWithPath("data.rooms[].roomOptionResponse.internet").type(JsonFieldType.BOOLEAN)
                        .description("객실 내 인터넷 여부"),
                    fieldWithPath("data.rooms[].roomOptionResponse.refrigerator").type(JsonFieldType.BOOLEAN)
                        .description("객실 내 냉장고 여부"),
                    fieldWithPath("data.rooms[].roomOptionResponse.toiletries").type(JsonFieldType.BOOLEAN)
                        .description("객실 내 세면도구 여부"),
                    fieldWithPath("data.rooms[].roomOptionResponse.sofa").type(JsonFieldType.BOOLEAN)
                        .description("객실 내 소파 여부"),
                    fieldWithPath("data.rooms[].roomOptionResponse.cooking").type(JsonFieldType.BOOLEAN)
                        .description("객실 내 취사용품 여부"),
                    fieldWithPath("data.rooms[].roomOptionResponse.table").type(JsonFieldType.BOOLEAN)
                        .description("객실 내 테이블 여부"),
                    fieldWithPath("data.rooms[].roomOptionResponse.hairDryer").type(JsonFieldType.BOOLEAN)
                        .description("객실 내 드라이기 여부"),
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

    @Test
    @DisplayName("예약 가능 여부를 확인할 수 있다.")
    @WithMockUser
    void isAvailableForReservation() throws Exception {
        // given
        Product product = productRepository.save(ProductFactory.createTestProduct());
        Room room = roomRepository.save(ProductFactory.createTestRoom(product));
        product.getRooms().add(room);
        ValueOperations<String, Object> values = restTemplate.opsForValue();
        values.set("roomId:" + String.valueOf(room.getId()) + ":" + "2023-12-22", "OK");
        // when
        ResultActions getProductAction = mockMvc.perform(

            get("/api/products/amounts/{roomId}?startDate=2023-12-22&endDate=2023-12-23",
                room.getId()));

        // then
        getProductAction
            .andExpect(status().isForbidden())
            .andDo(MockMvcResultHandlers.print())
            .andDo(restDoc.document(
                pathParameters(
                    parameterWithName("roomId").description("방 아이디")
                ),
                queryParameters(
                    parameterWithName("startDate").description("상세 검색, 체크인 일"),
                    parameterWithName("endDate").description("상세 검색, 체크아웃 일")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터")
                )
            ));
    }


}