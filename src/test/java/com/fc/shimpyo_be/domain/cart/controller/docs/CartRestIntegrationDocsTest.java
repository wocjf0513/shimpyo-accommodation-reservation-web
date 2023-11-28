package com.fc.shimpyo_be.domain.cart.controller.docs;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fc.shimpyo_be.config.RestDocsSupport;
import com.fc.shimpyo_be.domain.cart.dto.request.CartCreateRequest;
import com.fc.shimpyo_be.domain.cart.entity.Cart;
import com.fc.shimpyo_be.domain.cart.repository.CartRepository;
import com.fc.shimpyo_be.domain.member.entity.Authority;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.member.repository.MemberRepository;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.factory.ProductFactory;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import com.fc.shimpyo_be.global.util.SecurityUtil;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
class CartRestIntegrationDocsTest extends RestDocsSupport {

    @MockBean
    private SecurityUtil securityUtil;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ProductRepository productRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Room room;

    private Product product;

    private Member member;

    @BeforeEach
    void initTest() {
        //given
        member = memberRepository.save(memberRepository.save(
            Member.builder().email("wocjf0513@naver.com").photoUrl("hello,world.jpg").name("심재철")
                .password("1234").authority(Authority.ROLE_USER).build()));

        given(securityUtil.getCurrentMemberId()).willReturn(1L);

        product = productRepository.save(ProductFactory.createTestProduct());
        room = roomRepository.save(ProductFactory.createTestRoom(product));

        for (int i = 0; i < 5; i++) {
            Cart cart = cartRepository.save(
                Cart.builder().room(room).member(member).price(
                        100000L
                    ).startDate(LocalDate.now().minusDays(1))
                    .endDate(LocalDate.now()).build());
        }

        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @Test
    @WithMockUser
    void getCarts() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/carts"));

        //then
        resultActions.andExpect(status().isOk()).andDo(restDoc.document(
            responseFields(responseCommon()).and(
                fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
                fieldWithPath("data[].cartId").type(JsonFieldType.NUMBER).description("장바구니 아이디"),
                fieldWithPath("data[].productId").type(JsonFieldType.NUMBER).description("상품 아이디"),
                fieldWithPath("data[].productName").type(JsonFieldType.STRING).description("상품 이름"),
                fieldWithPath("data[].image").type(JsonFieldType.STRING).description("상품 대표 이미지"),
                fieldWithPath("data[].roomId").type(JsonFieldType.NUMBER).description("방 아이디"),
                fieldWithPath("data[].roomName").type(JsonFieldType.STRING).description("방 이름"),
                fieldWithPath("data[].price").type(JsonFieldType.NUMBER).description("총 가격"),
                fieldWithPath("data[].description").type(JsonFieldType.STRING).description("방 설명"),
                fieldWithPath("data[].standard").type(JsonFieldType.NUMBER).description("방 기준인원"),
                fieldWithPath("data[].capacity").type(JsonFieldType.NUMBER).description("방 최대인원"),
                fieldWithPath("data[].startDate").type(JsonFieldType.STRING).description("숙박 시작일"),
                fieldWithPath("data[].endDate").type(JsonFieldType.STRING).description("숙박 종료일"),
                fieldWithPath("data[].checkIn").type(JsonFieldType.STRING).description("방 체크인 시간"),
                fieldWithPath("data[].checkOut").type(JsonFieldType.STRING)
                    .description("방 체크아웃 시간"))));

    }

    @Test
    @WithMockUser
    void addCart() throws Exception {
        //given
        CartCreateRequest cartCreateRequest = CartCreateRequest.builder()
            .startDate("2023-11-27").endDate("2023-11-28").price(100000L).roomId(room.getId())
            .build();
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/carts")
            .content(objectMapper.writeValueAsString(cartCreateRequest))
            .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk()).andDo(restDoc.document(
            responseFields(responseCommon()).and(
                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                fieldWithPath("data.cartId").type(JsonFieldType.NUMBER).description("장바구니 아이디"),
                fieldWithPath("data.productId").type(JsonFieldType.NUMBER).description("상품 아이디"),
                fieldWithPath("data.productName").type(JsonFieldType.STRING).description("상품 이름"),
                fieldWithPath("data.image").type(JsonFieldType.STRING).description("상품 대표 이미지"),
                fieldWithPath("data.roomId").type(JsonFieldType.NUMBER).description("방 아이디"),
                fieldWithPath("data.roomName").type(JsonFieldType.STRING).description("방 이름"),
                fieldWithPath("data.price").type(JsonFieldType.NUMBER).description("총 가격"),
                fieldWithPath("data.description").type(JsonFieldType.STRING).description("방 설명"),
                fieldWithPath("data.standard").type(JsonFieldType.NUMBER).description("방 기준인원"),
                fieldWithPath("data.capacity").type(JsonFieldType.NUMBER).description("방 최대인원"),
                fieldWithPath("data.startDate").type(JsonFieldType.STRING).description("숙박 시작일"),
                fieldWithPath("data.endDate").type(JsonFieldType.STRING).description("숙박 종료일"),
                fieldWithPath("data.checkIn").type(JsonFieldType.STRING).description("방 체크인 시간"),
                fieldWithPath("data.checkOut").type(JsonFieldType.STRING)
                    .description("방 체크아웃 시간"))));
    }

    @Test
    @WithMockUser
    void deleteCart() throws Exception {
        //given

        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/carts/{cartId}",1L));
        //then
        resultActions.andExpect(status().isOk()).andDo(restDoc.document(
            responseFields(responseCommon()).and(
                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                fieldWithPath("data.cartId").type(JsonFieldType.NUMBER).description("장바구니 아이디"),
                fieldWithPath("data.roomId").type(JsonFieldType.NUMBER).description("방 아이디"),
                fieldWithPath("data.startDate").type(JsonFieldType.STRING).description("숙박 시작일"),
                fieldWithPath("data.endDate").type(JsonFieldType.STRING).description("숙박 종료일")
                )));
    }
}