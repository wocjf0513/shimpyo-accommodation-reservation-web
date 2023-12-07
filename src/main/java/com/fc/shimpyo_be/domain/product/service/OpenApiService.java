package com.fc.shimpyo_be.domain.product.service;

import com.fc.shimpyo_be.domain.product.entity.Address;
import com.fc.shimpyo_be.domain.product.entity.Category;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.entity.ProductImage;
import com.fc.shimpyo_be.domain.product.exception.OpenApiException;
import com.fc.shimpyo_be.domain.product.repository.ProductImageRepository;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.entity.RoomPrice;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenApiService {

    @Value("${open-api.service-key}")
    private String SERVICE_KEY;

    private final String BASE_URL = "https://apis.data.go.kr/B551011/KorService1";
    private final String DEFAULT_QUERY_PARAMS = "&MobileOS=ETC&MobileApp=AppTest&_type=json";
    private final int CONTENT_TYPE_ID = 32;

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final RoomRepository roomRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    private HttpEntity<String> httpEntity;

    @Transactional
    public void getData(int pageSize, int pageNum) throws JSONException {
        try {
            JSONObject accommodation = getAccommodation(pageSize, pageNum);
            JSONArray base = accommodation
                .getJSONObject("items")
                .getJSONArray("item");
            for (int j = 0; j < base.length(); j++) {
                JSONObject baseItem = base.getJSONObject(j);
                int contentId = baseItem.getInt("contentid");
                JSONObject infoBody = getInfo(contentId);
                if (isEmpty(infoBody)) {
                    log.info("반복 정보 조회에 데이터가 없습니다. 다음 숙박 숙소을 조회합니다.");
                    continue;
                }
                JSONArray info = infoBody.getJSONObject("items")
                    .getJSONArray("item");
                if (!hasRoom(info)) {
                    log.info("숙박 숙소에 방이 없습니다. 다음 숙박 숙소을 조회합니다.");
                    continue;
                }
                JSONObject imageBody = getImages(contentId);
                if (isEmpty(imageBody)) {
                    log.info("숙박 숙소에 이미지가 없습니다.다음 숙박 숙소을 조회합니다.");
                    continue;
                }
                JSONArray images = imageBody
                    .getJSONObject("items")
                    .getJSONArray("item");
                JSONObject commonBody = getCommon(contentId);
                if (isEmpty(commonBody)) {
                    log.info("공통 정보 조회에 데이터가 없습니다. 다음 숙박 숙소을 조회합니다.");
                    continue;
                }
                JSONObject common = commonBody.getJSONObject("items")
                    .getJSONArray("item")
                    .getJSONObject(0);
                JSONObject introBody = getIntro(contentId);
                if (isEmpty(introBody)) {
                    log.info("소개 정보 조회에 데이터가 없습니다. 다음 숙박 숙소을 조회합니다.");
                    continue;
                }
                JSONObject intro = introBody
                    .getJSONObject("items")
                    .getJSONArray("item")
                    .getJSONObject(0);
                if (baseItem.getString("firstimage").isEmpty()) {
                    log.info("썸네일로 사용할 이미지가 없습니다. 다음 숙박 숙소을 조회합니다.");
                    continue;
                }
                Product product = saveProduct(baseItem, common);
                saveImages(product, images);
                saveRooms(product, intro, info);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new OpenApiException();
        }
    }

    private String makeBaseSearchUrl() {
        String STAY_SEARCH_URI = "/searchStay1";
        return BASE_URL + STAY_SEARCH_URI +
            "?serviceKey=" + URLEncoder.encode(SERVICE_KEY, StandardCharsets.UTF_8) +
            DEFAULT_QUERY_PARAMS;
    }

    private String makeUrl(String URL) {
        int numOfRows = 30;
        return BASE_URL + URL +
            "?serviceKey=" + URLEncoder.encode(SERVICE_KEY, StandardCharsets.UTF_8) +
            DEFAULT_QUERY_PARAMS +
            "&numOfRows=" + numOfRows;
    }

    private JSONObject getAccommodation(int pageSize, int pageNum) throws JSONException {
        URI uri = UriComponentsBuilder.fromHttpUrl(makeBaseSearchUrl())
            .queryParam("pageNo", pageNum)
            .queryParam("numOfRows", pageSize)
            .build(true).toUri();
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET,
            httpEntity, String.class);
        log.info("숙박 정보 조회");
        return new JSONObject(response.getBody())
            .getJSONObject("response")
            .getJSONObject("body");
    }

    private JSONObject getCommon(long contentId) throws JSONException {
        URI uri = UriComponentsBuilder.fromHttpUrl(makeUrl("/detailCommon1"))
            .queryParam("pageNo", 1)
            .queryParam("contentId", contentId)
            .queryParam("defaultYN", "Y")
            .queryParam("firstImageYN", "Y")
            .queryParam("areacodeYN", "Y")
            .queryParam("catcodeYN", "Y")
            .queryParam("addrinfoYN", "Y")
            .queryParam("overviewYN", "Y")
            .build(true).toUri();
        ResponseEntity<String> commonResponse = restTemplate.exchange(uri, HttpMethod.GET,
            httpEntity, String.class);
        log.info(contentId + "번 데이터 공통 정보 조회" + commonResponse.getBody());
        return new JSONObject(commonResponse.getBody())
            .getJSONObject("response")
            .getJSONObject("body");
    }

    private JSONObject getIntro(long contentId) throws JSONException {
        URI uri = UriComponentsBuilder.fromHttpUrl(makeUrl("/detailIntro1"))
            .queryParam("pageNo", 1)
            .queryParam("contentId", contentId)
            .queryParam("contentTypeId", CONTENT_TYPE_ID)
            .build(true).toUri();
        ResponseEntity<String> introResponse = restTemplate.exchange(uri, HttpMethod.GET,
            httpEntity, String.class);
        log.info(contentId + "번 데이터 소개 정보 조회" + introResponse.getBody());
        return new JSONObject(introResponse.getBody())
            .getJSONObject("response")
            .getJSONObject("body");
    }

    private JSONObject getInfo(long contentId) throws JSONException {
        URI uri = UriComponentsBuilder.fromHttpUrl(makeUrl("/detailInfo1"))
            .queryParam("pageNo", 1)
            .queryParam("contentId", contentId)
            .queryParam("contentTypeId", CONTENT_TYPE_ID)
            .build(true).toUri();
        ResponseEntity<String> infoResponse = restTemplate.exchange(uri, HttpMethod.GET,
            httpEntity, String.class);
        log.info(contentId + "번 데이터 반복 정보 조회" + infoResponse.getBody());
        return new JSONObject(infoResponse.getBody())
            .getJSONObject("response")
            .getJSONObject("body");
    }

    private JSONObject getImages(long contentId) throws JSONException {
        URI uri = UriComponentsBuilder.fromHttpUrl(makeUrl("/detailImage1"))
            .queryParam("pageNo", 1)
            .queryParam("contentId", contentId)
            .queryParam("imageYN", "Y")
            .queryParam("subImageYN", "Y")
            .build(true).toUri();
        ResponseEntity<String> imageResponse = restTemplate.exchange(uri, HttpMethod.GET,
            httpEntity, String.class);
        log.info(contentId + "번 데이터 이미지 정보 조회" + imageResponse.getBody());
        return new JSONObject(imageResponse.getBody())
            .getJSONObject("response")
            .getJSONObject("body");
    }

    private boolean hasRoom(JSONArray info) throws JSONException {
        boolean hasRoom = false;
        for (int i = 0; i < info.length(); i++) {
            if (Integer.parseInt(info.getJSONObject(i).getString("roomcount")) != 0) {
                hasRoom = true;
            }
        }
        return hasRoom;
    }

    private Product saveProduct(JSONObject base, JSONObject common) throws JSONException {
        Product product = Product.builder()
            .name(base.getString("title"))
            .category(Category.getByCode(base.getString("cat3")))
            .address(
                Address.builder()
                    .address(base.getString("addr1"))
                    .detailAddress(base.getString("addr2"))
                    .mapX(base.getDouble("mapx"))
                    .mapY(base.getDouble("mapy"))
                    .build()
            )
            .description(common.getString("overview"))
            .starAvg(0)
            .thumbnail(base.getString("firstimage"))
            .build();
        return productRepository.save(product);
    }

    private void saveImages(Product product, JSONArray images) throws JSONException {
        List<ProductImage> productImages = new ArrayList<>();
        for (int k = 0; k < images.length(); k++) {
            productImages.add(ProductImage.builder()
                .product(product)
                .photoUrl(images.getJSONObject(k).getString("originimgurl"))
                .build());
        }
        productImageRepository.saveAll(productImages);
    }

    private void saveRooms(Product product, JSONObject intro, JSONArray info) throws JSONException {
        List<Room> rooms = new ArrayList<>();
        for (int k = 0; k < info.length(); k++) {
            JSONObject roomJson = info.getJSONObject(k);
            if (Integer.parseInt(roomJson.getString("roomcount")) != 0) {
                for (int r = 0; r < Integer.parseInt(roomJson.getString("roomcount"));
                    r++) {
                    System.out.println(intro.toString());
                    String[] checkIn = intro.getString("checkintime").split(":|;");
                    String[] checkOut = intro.getString("checkouttime").split(":|;");
                    System.out.println(roomJson);
                    rooms.add(Room.builder()
                        .product(product)
                        .name(roomJson.getString("roomtitle"))
                        .description(roomJson.getString("roomintro"))
                        .standard(
                            roomJson.getInt("roombasecount"))
                        .capacity(Math.max(roomJson.getInt("roombasecount"),
                            roomJson.getInt("roommaxcount")))
                        .checkIn(LocalTime.of(
                            Integer.parseInt(checkIn[0].substring(checkIn[0].length() - 2)),
                            Integer.parseInt(checkIn[1].substring(0, 2))))
                        .checkOut(LocalTime.of(
                            Integer.parseInt(checkOut[0].substring(checkOut[0].length() - 2)),
                            Integer.parseInt(checkOut[1].substring(0, 2))))
                        .price(
                            RoomPrice.builder()
                                .offWeekDaysMinFee(Integer.parseInt(
                                    roomJson.getString("roomoffseasonminfee1")))
                                .offWeekendMinFee(Integer.parseInt(
                                    roomJson.getString("roomoffseasonminfee12")))
                                .peakWeekDaysMinFee(Integer.parseInt(
                                    roomJson.getString("roompeakseasonminfee1")))
                                .peakWeekendMinFee(Integer.parseInt(
                                    roomJson.getString("roompeakseasonminfee2")))
                                .build()
                        )
                        .build());
                }
            }
        }
        roomRepository.saveAll(rooms);
    }

    private boolean isEmpty(JSONObject body) throws JSONException {
        return body.getInt("totalCount") == 0;
    }
}
