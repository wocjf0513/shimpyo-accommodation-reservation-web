package com.fc.shimpyo_be.domain.product.service;

import com.fc.shimpyo_be.domain.product.entity.Address;
import com.fc.shimpyo_be.domain.product.entity.Amenity;
import com.fc.shimpyo_be.domain.product.entity.Category;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.entity.ProductImage;
import com.fc.shimpyo_be.domain.product.entity.ProductOption;
import com.fc.shimpyo_be.domain.product.exception.InvalidDataException;
import com.fc.shimpyo_be.domain.product.exception.OpenApiException;
import com.fc.shimpyo_be.domain.product.repository.ProductImageRepository;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.entity.RoomImage;
import com.fc.shimpyo_be.domain.room.entity.RoomOption;
import com.fc.shimpyo_be.domain.room.entity.RoomPrice;
import com.fc.shimpyo_be.domain.room.repository.RoomImageRepository;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import jakarta.annotation.PostConstruct;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
    private final RoomImageRepository roomImageRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    private HttpEntity<String> httpEntity;

    @PostConstruct
    public void init() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        httpEntity = new HttpEntity<>(headers);
    }

    @Transactional
    public void getData(int pageSize, int pageNum) throws JSONException {
        try {
            JSONArray stayArr = getItems(getAccommodation(pageSize, pageNum));

            for (int j = 0; j < stayArr.length(); j++) {
                try {
                    JSONObject stay = stayArr.getJSONObject(j);
                    int contentId = stay.getInt("contentid");
                    JSONObject info = getInfo(contentId);
                    checkInfo(info);
                    JSONArray rooms = getItems(info);
                    checkRoom(rooms);
                    JSONObject image = getImages(contentId);
                    checkImage(image);
                    JSONArray images = getItems(image);
                    JSONObject common = getCommon(contentId);
                    checkCommon(common);
                    JSONObject commonItem = getItems(common).getJSONObject(0);
                    JSONObject intro = getIntro(contentId);
                    checkIntro(intro);
                    JSONObject introItem = getItems(intro).getJSONObject(0);
                    checkIntroItem(introItem);
                    checkStay(stay);
                    Product product = saveProduct(stay, commonItem, introItem);
                    saveProductImages(product, images);
                    saveRooms(product, introItem, rooms);
                } catch (InvalidDataException e) {
                    log.error(e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("[OpenAPI] " + e.getMessage());
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
        return getBody(response.getBody());
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
        return getBody(commonResponse.getBody());
    }

    private JSONObject getIntro(long contentId) throws JSONException {
        URI uri = UriComponentsBuilder.fromHttpUrl(makeUrl("/detailIntro1"))
            .queryParam("pageNo", 1)
            .queryParam("contentId", contentId)
            .queryParam("contentTypeId", CONTENT_TYPE_ID)
            .build(true).toUri();
        ResponseEntity<String> introResponse = restTemplate.exchange(uri, HttpMethod.GET,
            httpEntity, String.class);
        return getBody(introResponse.getBody());
    }

    private JSONObject getInfo(long contentId) throws JSONException {
        URI uri = UriComponentsBuilder.fromHttpUrl(makeUrl("/detailInfo1"))
            .queryParam("pageNo", 1)
            .queryParam("contentId", contentId)
            .queryParam("contentTypeId", CONTENT_TYPE_ID)
            .build(true).toUri();
        ResponseEntity<String> infoResponse = restTemplate.exchange(uri, HttpMethod.GET,
            httpEntity, String.class);
        return getBody(infoResponse.getBody());
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
        return getBody(imageResponse.getBody());
    }

    private JSONArray getItems(JSONObject jsonObject) {
        return jsonObject.getJSONObject("items").getJSONArray("item");
    }

    private JSONObject getBody(String source) {
        return new JSONObject(source).getJSONObject("response").getJSONObject("body");
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

    private Product saveProduct(JSONObject base, JSONObject common, JSONObject intro)
        throws JSONException {
        ProductOption productOption = ProductOption.builder()
            .cooking(intro.get("chkcooking").equals("가능"))
            .parking(intro.get("parkinglodging").equals("가능"))
            .pickup(intro.get("pickup").equals("가능"))
            .foodPlace(intro.getString("foodplace"))
            .infoCenter(intro.getString("infocenterlodging"))
            .build();
        Amenity amenity = Amenity.builder()
            .barbecue(intro.get("barbecue").equals("1"))
            .beauty(intro.get("beauty").equals("1"))
            .beverage(intro.get("beverage").equals("1"))
            .bicycle(intro.get("bicycle").equals("1"))
            .campfire(intro.get("campfire").equals("1"))
            .fitness(intro.get("fitness").equals("1"))
            .karaoke(intro.get("karaoke").equals("1"))
            .publicBath(intro.get("publicbath").equals("1"))
            .publicPc(intro.get("publicpc").equals("1"))
            .sauna(intro.get("sauna").equals("1"))
            .sports(intro.get("sports").equals("1"))
            .seminar(intro.get("seminar").equals("1"))
            .build();
        Product product = Product.builder()
            .name(base.getString("title"))
            .address(
                Address.builder()
                    .address(base.getString("addr1"))
                    .detailAddress(base.getString("addr2"))
                    .mapX(base.getDouble("mapx"))
                    .mapY(base.getDouble("mapy"))
                    .build()
            )
            .category(Category.getByCode(base.getString("cat3")))
            .description(common.getString("overview"))
            .starAvg(0)
            .thumbnail(base.getString("firstimage"))
            .photoUrls(new ArrayList<>())
            .productOption(productOption)
            .amenity(amenity)
            .build();
        return productRepository.save(product);
    }

    private void saveProductImages(Product product, JSONArray images) {
        for (int k = 0; k < images.length(); k++) {
            productImageRepository.save(ProductImage.builder()
                .product(product)
                .photoUrl(images.getJSONObject(k).getString("originimgurl"))
                .build());
        }
    }

    private void saveRooms(Product product, JSONObject intro, JSONArray info) throws JSONException {
        for (int i = 0; i < info.length(); i++) {
            JSONObject roomJson = info.getJSONObject(i);
            if (roomJson.getInt("roombasecount") == 0 && roomJson.getInt("roommaxcount") == 0) {
                continue;
            }
            if (Integer.parseInt(roomJson.getString("roomcount")) != 0) {
                for (int j = 0; j < Integer.parseInt(roomJson.getString("roomcount")); j++) {
                    String[] stringCheckIn = intro.getString("checkintime").split(":|;|시");
                    String[] stringCheckOut = intro.getString("checkouttime").split(":|;|시");
                    LocalTime checkIn = getTimeFromString(stringCheckIn);
                    LocalTime checkOut = getTimeFromString(stringCheckOut);

                    RoomPrice roomPrice = RoomPrice.builder()
                        .offWeekDaysMinFee(Integer.parseInt(
                            roomJson.getString("roomoffseasonminfee1")))
                        .offWeekendMinFee(Integer.parseInt(
                            roomJson.getString("roomoffseasonminfee2")))
                        .peakWeekDaysMinFee(Integer.parseInt(
                            roomJson.getString("roompeakseasonminfee1")))
                        .peakWeekendMinFee(Integer.parseInt(
                            roomJson.getString("roompeakseasonminfee2")))
                        .build();
                    RoomOption roomOption = RoomOption.builder()
                        .bathFacility(roomJson.get("roombathfacility").equals("Y"))
                        .bath(roomJson.get("roombath").equals("Y"))
                        .homeTheater(roomJson.get("roomhometheater").equals("Y"))
                        .airCondition(roomJson.get("roomaircondition").equals("Y"))
                        .tv(roomJson.get("roomtv").equals("Y"))
                        .pc(roomJson.get("roompc").equals("Y"))
                        .cable(roomJson.get("roomcable").equals("Y"))
                        .internet(roomJson.get("roominternet").equals("Y"))
                        .refrigerator(roomJson.get("roomrefrigerator").equals("Y"))
                        .toiletries(roomJson.get("roomtoiletries").equals("Y"))
                        .sofa(roomJson.get("roomsofa").equals("Y"))
                        .cooking(roomJson.get("roomcook").equals("Y"))
                        .diningTable(roomJson.get("roomtable").equals("Y"))
                        .hairDryer(roomJson.get("roomhairdryer").equals("Y"))
                        .build();
                    Room room = roomRepository.save(Room.builder()
                        .product(product)
                        .code(roomJson.getLong("roomcode"))
                        .name(roomJson.getString("roomtitle"))
                        .description(roomJson.getString("roomintro"))
                        .standard(
                            roomJson.getInt("roombasecount"))
                        .capacity(Math.max(roomJson.getInt("roombasecount"),
                            roomJson.getInt("roommaxcount")))
                        .checkIn(checkIn)
                        .checkOut(checkOut)
                        .price(roomPrice)
                        .roomOption(roomOption)
                        .roomImages(new ArrayList<>())
                        .build());
                    for (int k = 1; k <= 5; k++) {
                        if (!roomJson.get("roomimg" + k).equals("")) {
                            roomImageRepository.save(RoomImage.builder()
                                .room(room)
                                .photoUrl(roomJson.getString("roomimg" + k))
                                .description(roomJson.getString("roomimg" + k + "alt"))
                                .build());
                        }
                    }
                }
            }
        }
    }

    private boolean isEmpty(JSONObject body) throws JSONException {
        return body.getInt("totalCount") == 0;
    }

    private LocalTime getTimeFromString(String[] stringTime) {
        int hour = Integer.parseInt(
            stringTime[0].trim().substring(stringTime[0].trim().length() - 2));
        int minute =
            stringTime.length == 1 ? 0 : Integer.parseInt(stringTime[1].trim().substring(0, 2));
        return LocalTime.of(hour, minute);
    }

    private void checkInfo(JSONObject info) {
        if (isEmpty(info)) {
            throw new InvalidDataException("반복 정보 조회에 데이터가 없습니다. 다음 숙소를 조회합니다.");
        }
    }

    private void checkRoom(JSONArray rooms) {
        if (!hasRoom(rooms)) {
            throw new InvalidDataException("숙박 숙소에 방이 없습니다. 다음 숙소를 조회합니다.");
        }
    }

    private void checkImage(JSONObject image) {
        if (isEmpty(image)) {
            throw new InvalidDataException("숙박 숙소에 이미지가 없습니다.다음 숙소를 조회합니다.");
        }
    }

    private void checkCommon(JSONObject common) {
        if (isEmpty(common)) {
            throw new InvalidDataException("공통 정보 조회에 데이터가 없습니다. 다음 숙소를 조회합니다.");
        }
    }

    private void checkIntro(JSONObject intro) {
        if (isEmpty(intro)) {
            throw new InvalidDataException("소개 정보 조회에 데이터가 없습니다. 다음 숙소를 조회합니다.");
        }
    }

    private void checkIntroItem(JSONObject introItem) {
        if (introItem.getString("checkintime").trim().isEmpty() || introItem.getString(
            "checkouttime").trim().isEmpty()) {
            throw new InvalidDataException("체크인 체크아웃 데이터가 없습니다. 다음 숙소를 조회합니다. ");
        }
        if (introItem.getString("checkintime").split(":|;|시").length != 2
            || introItem.getString("checkouttime").split(":|;|시").length != 2) {
            throw new InvalidDataException("체크인 체크아웃 데이터가 형식에 맞지 않습니다. 다음 숙소를 조회합니다.");
        }
    }

    private void checkStay(JSONObject stay) {
        if (stay.getString("firstimage").isEmpty()) {
            throw new InvalidDataException("썸네일로 사용할 이미지가 없습니다. 다음 숙소를 조회합니다.");
        }
    }
}
