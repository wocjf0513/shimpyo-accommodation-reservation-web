# Shimpyo_BE : 숙박 정보 조회/검색 및 숙박 예약 시스템

---

## 0. 목차

- [1. 프로젝트 설명](#1-프로젝트-설명)
    - [🧑🏻‍💻 팀원](#-팀원)
    - [💁🏻 소개](#-소개)
    - [🎯 목적](#-목적)
    - [🔗 배포 링크](#-배포-링크)
- [2. 설정](#2-설정)
- [3. 설계](#3-설계)
    - [📰 아키텍처](#-아키텍처)
    - [💾 DB 설계](#-DB-설계)
    - [📡 API 설계](#-API-설계)
- [4. CI/CD](#4-cicd)
- [5. 리팩토링 이후 추가된 기능](#5-리팩토링-이후-추가된-기능)
- [6. 에러 해결](#6-에러-해결)
- [7. 회고](#7-회고)
- [8. API 문서](#8-API-문서)

---

## 1. 프로젝트 설명

### 🧑🏻‍💻 팀원

<table>
  </tr>
    <tr>
    <td align="center" width="150px">
      <a href="https://github.com/JeongUijeong" target="_blank">
        <img src="https://github.com/Shimpyo-House/Shimpyo_FE/assets/98576512/bc2ef8d5-a063-4473-991b-92df77fb0263" alt="정의정 프로필" />
      </a>
    </td>
    <td align="center" width="150px">
      <a href="https://github.com/wocjf0513" target="_blank">
        <img src="https://github.com/Shimpyo-House/Shimpyo_FE/assets/98576512/b6cdb1b6-76dd-4136-a6c5-eecb9ca0c2ab" alt="심재철 프로필" />
      </a>
    </td>
    <td align="center" width="150px">
      <a href="https://github.com/jo0oy" target="_blank">
        <img src="https://github.com/Shimpyo-House/Shimpyo_FE/assets/98576512/29a0dfc6-8e91-4f5f-a5bd-43b7b668f4a6" alt="이주연 프로필" />
      </a>
    </td>
  </tr>
  <tr>
    <td align="center">
      <a href="https://github.com/JeongUijeong" target="_blank">
        정의정<br />
        BE 팀장
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/wocjf0513" target="_blank">
        심재철<br />
        BE 팀원
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/jo0oy" target="_blank">
        이주연<br />
        BE 팀원
      </a>
    </td>
  </tr>
  <tr>
    <td align="left">
      - CI<br />
      - REST Docs<br />
      - 회원<br />
      - 즐겨찾기<br />
    </td>
    <td align="left">
      - CD<br />
      - EC2<br />
      - 숙소<br />
      - 장바구니<br />
    </td>
    <td align="left">
      - Redis 분산락<br />
      - 예약<br />
      - 별점<br />
    </td>
  </tr>
</table>
<table>
  <tr>
    <td align="center" width="150px">
      <a href="https://github.com/TaePoong719" target="_blank">
        <img src="https://avatars.githubusercontent.com/u/98576512?v=4" alt="최우혁 프로필" />
      </a>
    </td>
    <td align="center" width="150px">
      <a href="https://github.com/jiohjung98" target="_blank">
        <img src="https://github.com/KDT1-FE/Y_FE_Toy1/assets/39702832/58fb577d-9f8c-4679-bca1-8ff15ca84f6b" alt="백상원 프로필" />
      </a>
    </td>
    <td align="center" width="150px">
      <a href="https://github.com/Yamyam-code" target="_blank">
        <img src="https://github.com/Shimpyo-House/Shimpyo_FE/assets/104253583/cab3f3cc-ccb0-46cc-8b36-bf645b5c4086" alt="정지오 프로필" />
      </a>
    </td>
    <td align="center" width="150px">
      <a href="https://github.com/wkdtnqls0506" target="_blank">
        <img src="https://github.com/Shimpyo-House/Shimpyo_FE/assets/93272421/9b7ea286-4768-4d55-a26e-fc0541824b71" alt="장수빈 프로필" />
      </a>
    </td>
    <td align="center" width="150px">
      <a href="https://github.com/seacrab808" target="_blank">
        <img src="https://github.com/Shimpyo-House/Shimpyo_FE/assets/93272421/6b9601d2-eec9-4887-8041-f98ed2f319d2" alt="소유나 프로필" />
      </a>
    </td>

  </tr>
  <tr>
    <td align="center">
      <a href="https://github.com/TaePoong719" target="_blank">
        최우혁<br />
        FE 팀장(전)
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/Yamyam-code" target="_blank">
        백상원<br />
        FE 팀장
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/jiohjung98" target="_blank">
        정지오<br />
        FE 팀원
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/wkdtnqls0506" target="_blank">
        장수빈<br />
        FE 팀원
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/seacrab808" target="_blank">
        소유나<br />
        FE 팀원
      </a>
    </td>
  </table>
<br>

### 💁🏻 소개

![](src/main/resources/image/main-page.png)
> 숙박 정보 조회/검색 및 숙박 예약 시스템

### 🎯 목적

- 숙박 시설 제공 및 검색: 사용자가 특정 지역이나 날짜에 필요한 숙박 시설을 쉽게 찾을 수 있도록 정보를 제공합니다.
- 가용한 숙소 정보 제공: 사용자에게 해당 지역의 여러 숙소 옵션, 가격, 시설, 평가 등 다양한 정보를 제공하여 선택을 돕습니다.
- 온라인 예약 및 결제: 사용자가 선택한 숙소를 신속하게 예약하고, 온라인으로 결제할 수 있는 편리한 기능을 제공합니다.
- 평가 제공: 다른 이용자들의 평가를 통해 사용자가 숙소를 선택할 때 도움을 받을 수 있도록 합니다.

### ⏰ 개발 기간

2023-11-20 ~ 2023-12-01

### 🔗 배포 링크

- BE: https://43.202.234.108.nip.io/
- FE: https://shimpyo.netlify.app/

### 🪄 실행 가이드

1. Clone 받는다.

```shell
git clone https://github.com/Shimpyo-House/Shimpyo_BE.git
cd Shimpyo_BE
```

2. 설정 파일을 작성한다.

> `application.yaml`
>
> ```java
> spring:
>   profiles:
>     active: local

> `application-local.yaml`
> ```java
> spring:
>   config:
>     activate:
>       on-profile: local
> datasource:
>   driver-class-name: org.mariadb.jdbc.Driver
>   url: jdbc:mariadb://localhost:3308/shimpyo?createDatabaseIfNotExist=true&serverTimezone=Asia/Seoul
>   username: ${DB_USERNAME}
>   password: ${DB_PASSWORD}
> jpa:
>   hibernate:
>     ddl-auto: update
>   show-sql: true
>   properties:
>     hibernate:
>     default_batch_fetch_size: 100
>   data:
>     redis:
>       host: localhost
>       port: 6379
> jwt:
>   secret: ${JWT_SECRET}
>   access-token-expire-time: ${ACCEESS_TOKEN_EXPIRE_TIME}
>   refresh-token-expire-time: ${REFRESH_TOKEN_EXPIRE_TIME}
> open-api:
>   service-key: F+zEWUXK0YK5FtuiHBJpOdqfmCV5qNXt+0F5g7X67//VQnlWbQCMUF5UlKPMetolbj9LAbfC0o7+XBe2AyzaWQ==
> logging:
>   level:
>     root: debug

3. Docker 컨테이너 실행

```shell
docker-compose up --build -d
```

4. Run

```shell
gradle test
gradle build
gradle bootJar
```

5. 테스트 계정
- EMAIL: test@mail.com
- PW: qwer1234!!

---

## 2. 설정

- 자바 버전: 17
- 스프링 버전: 6.0.13
- 스프링 부트 버전: 3.1.5
- 의존성
    - security, JWT
    - Lombok
    - Spring REST Docs
    - Spring Data Jpa
    - Validation
    - QueryDSL
    - Spring Web
    - Test Containers
    - Json
- `application.yaml`, `applicaion-local.yaml`, `application-prod.yaml`, `.env` 파일은 LMS에서 확인하실 수
  있습니다!

---

## 3. 설계

### 📰 아키텍처

> ![](src/main/resources/image/architecture.png)

### 💾 DB 설계

`ERD`
> ![](src/main/resources/image/erd.png)

### 📡 API 설계

[Spring REST Docs](#API-문서)를 통해 확인하실 수 있습니다.

---

## 4. CI/CD

### 🛠️ CI

> ![](src/main/resources/image/ci.png)

### 🛠️ CD

> ![](src/main/resources/image/cd.png)

---

## 5. 리팩토링 이후 추가된 기능
### 1. 즐겨찾기

#### ① 즐겨찾기 등록 기능

회원은 숙소를 즐겨찾기(찜) 할 수 있다.

#### ② 즐겨찾기 목록 조회 기능

회원은 본인이 즐겨찾기(찜) 한 숙소 목록을 조회할 수 있다.

페이지네이션이 가능하다.

#### ③ 즐겨찾기 취소 기능

회원은 숙소에 대한 즐겨찾기(찜)을 취소할 수 있다.

### 2. 숙소

#### ① 숙소 검색 시, 수용인원에 따른 검색 추가

숙소 검색 시, 숙박할 인원에 따른 숙소 검색이 가능해졌습니다.

#### ② 숙소 검색 시, 성수기, 비수기, 평일, 주말에 따라 가격 표시 다르게 조회

현재 날짜에 따라, 가격이 다르게 조회될 수 있게 변경했습니다.

#### ② 숙소 상세 검색 시, 성수기, 비수기, 평일, 주말에 따라 가격 표시 다르게 조회

현재 날짜에 따라, 가격이 다르게 조회될 수 있게 변경했습니다.

### 3. 예약 선점

#### ① 예약 선점 로직 수정

기존

- 예약 선점이 가능한지 검증
- 검증 결과가 true일 경우, 예약 선점 —> 선점한 레디스 키 만료기한 : ‘숙박 마지막일’ 일시
  - 그러나 결제 완료되지 않거나, 브라우저 창을 닫는 경우 선점이 취소되야 합니다.

변경

- 예약 선점시 결제가 완료되지 않거나 브라우저가 종료된 경우,자동으로 선점이 취소될 수 있도록 만료기한을 30분으로 변경했습니다. → 예약 결제시 만료기한을 ‘숙박 마지막일’로 재설정하도록 수정

### 4. 예약 주문

#### ① 예약 주문 페이지에서 필요한 숙소/객실 정보 리스트 조회 API 추가

주문/결제 페이지에서 기존에 FE끼리 주고 받던 데이터를 백엔드에 요청해 조회하도록 API를 추가 구현했습니다.

#### ② 예약 주문 API Request Body 수정

예약 주문 API Request Body를 수정했습니다. (불필요한 요청값 제거 + 장바구니 식별자 필드(cartId) 추가)

#### ③ 예약 주문 로직에서 선점 레디스 키 만료기한 재설정 하도록 수정

기존의 예약 선점 서비스 로직이 변경됨에 따라 예약 주문 저장 로직이 변경되었습니다.

예약 저장 서비스 로직에서 선점했던 레디스 키의 만료기한을 ‘체크아웃’ 기한까지로 재설정합니다.

#### ④ 예약 주문 완료 시 예약 완료된 장바구니 아이템을 삭제하는 로직 추가

예약 주문 API Request Body에 cartId 정보를 추가로 받아 예약 주문 로직에서 정상적으로 예약이 저장된 경우 해당 장바구니 아이템을 삭제하는 로직을 추가 구현했습니다.

### 5. 별점

#### ① 별점 등록 가능 기한(만료기한) 검증 로직 추가

별점 등록 가능 기한을 체크아웃 이후 2주내로 제한한다는 요구사항이 추가됨에 따라 별점 등록시 등록이 가능한 기간인지 검증하는 로직을 추가했습니다.

---

### 4. Open API 추가 데이터 활용

#### ① 숙소 옵션, 부대시설 데이터 추가 활용

<details>
<summary>숙소 옵션</summary>
<div markdown="1">

- 개실 내 취사 여부
- 주차시설 여부
- 픽업 서비스 여부
- 식음료장
- 문의 및 안내
- 
</div>
</details>

<details>
<summary>숙소 부대시설</summary>
<div markdown="1">

- 바비큐장 여부
- 뷰티시설 정보
- 식음료장 여부
- 자전거 대여 여부
- 캠프파이어 여부
- 휘트니스 센터 여부
- 노래방 여부
- 공동 샤워실 여부
- 공동 PC실 여부
- 사우나실 여부
- 스포츠 시설 여부
- 세미나실 여부

</div>
</details>


#### ② 객실 이미지, 옵션 데이터 추가 활용

<details>
<summary>객실 이미지</summary>
<div markdown="1">

- 객실 이미지 URL

</div>
</details>

<details>
<summary>객실 옵션</summary>
<div markdown="1">

- 목욕 시설 여부
- 욕조 여부
- 홈시어터 여부
- 에어컨 여부
- TV 여부
- PC 여부
- 케이블 설치 여부
- 인터넷 여부
- 냉장고 여부
- 세면도구 여부
- 소파 여부
- 취사 용품 여부
- 테이블 여부
- 드라이기 여부

</div>
</details>

<details>
<summary>객실 가격</summary>
<div markdown="1">

- 비수기 주중 최소 가격
- 비수기 주말 최소 가격
- 성수기 주중 최소 가격
- 성수기 주말 최소 가격

</div>
</details>

#### ③ 변경된 API

1.  숙소 전체 및 검색 조회 API 변경 (위와 같은 데이터가 추가 응답됩니다.)
2.  객실 조회 API 변경
3.  장바구니 조회 API 변경

---

## 6. 에러 해결

### 📌 주요 에러

> #### < Spring REST Docs snippet 에러 >
> Spring REST Docs 으로 API를 문서화 하기 위해 asciidoc을 사용합니다.
>
> 그런데 테스트 코드를 작성하고 빌드를 수행하면 만들어지는 html을 확인해보니,  **snippets 파일을 찾지 못 하는 버그**가 발견되었습니다.
>
> ![](src/main/resources/image/docs-error.png)
> ```shell
> 11월 27, 2023 7:22:00 오후 uri:classloader:/gems/asciidoctor-2.0.10/lib/asciidoctor/reader.rb preprocess_include_directive
> SEVERE: member-api.adoc: line 15: include file not found: C:/Users/jeong/Desktop/FC/Shimpyo_BE/{snippets}/auth-rest-controller-docs-test/sign-up/http-request.adoc
> include file not found: C:/Users/jeong/Desktop/FC/Shimpyo_BE/{snippets}/auth-rest-controller-docs-test/sign-up/http-request.adoc :: member-api.adoc :: C:/Users/jeong/Desktop/FC/Shimpyo_BE/C:/Users/jeong/Desktop/FC/Shimpyo_BE/src/docs/asciidoc/member/member-api.adoc:15 (uri:classloader:/gems/asciidoctor-2.0.10/lib/asciidoctor/reader.rb:preprocess_include_directive)
> ```
> #### < 에러 해결 >
> snippets 경로를 index.adoc 파일이 아니라 member-api.adoc 파일에서 지정해보니, 정상적으로 작동하는 것을 확인할 수 있었습니다.
>
> ```shell
> ifndef::snippets[]
> :snippets: build/generated-snippets
> endif::[]
>
> = Member REST API Docs
> :doctype: book
> :icons: font
> :source-highlighter: highlightjs
> :toc: left
> :toclevels: 2
> 
> ~~ 생략 ~~
> ```

> #### < Open API Service Key 에러 >
> Open API 에서 숙박 상품 조회를 하기 위해 요청을 보냈는데, 다음과 같은 응답을 받았습니다.
>
> ```shell
> <OpenAPI_ServiceResponse>
>   <cmmMsgHeader>
>     <errMsg>SEVICE ERROR</errMsg>
>     <returnAuthMsg>SERVICE_KEY_IS_NOT_REGISTERED_ERROR</returnAuthMsg>
>     <returnReasonCode>30</returnReasonCode>
>   </cmmMsgHeader>
> </OpenAPI_ServiceResponse>
> ```
> Service Key가 등록되지 않았다는 에러 응답이었습니다.
>
> 디코딩 서비스 키를 사용해보기도 하고 인코딩 서비스키를 사용해보기도 했으나 여전히 같은 응답이 돌아왔습니다.
>
> #### < 에러 해결 >
> Service 키를 먼저 인코딩 한 후 Uri 빌드 시 true 옵션을 부여하여 해결했습니다.
> ```java
> // ~~ 생략 ~~
> private String makeBaseSearchUrl() {
>     String STAY_SEARCH_URI = "/searchStay1";
>     return BASE_URL + STAY_SEARCH_URI +
>         "?serviceKey=" + URLEncoder.encode(SERVICE_KEY, StandardCharsets.UTF_8) +
>         DEFAULT_QUERY_PARAMS;
> }
> // ~~생략~~
> private JSONObject getAccommodation(int pageSize, int pageNum) throws JSONException {
>     URI uri = UriComponentsBuilder.fromHttpUrl(makeBaseSearchUrl())
>         .queryParam("pageNo", pageNum)
>         .queryParam("numOfRows", pageSize)
>         .build(true).toUri();
>     ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET,
>         httpEntity, String.class);
>     log.info("숙박 정보 조회");
>     return new JSONObject(response.getBody())
>         .getJSONObject("response")
>         .getJSONObject("body");
> }
> // ~~ 생략 ~~

> #### < 상수화 Wrapper Type 에러 >
> 상수 조건 값을 Wrapper Type으로 선언해 빌드시 롬복 자체 식별이 안되는 에러가 발생했습니다.
> 
> ![](src/main/resources/image/wrapper-error-1.png)
>
> #### < 에러 해결 >
> 유효성 검증 값의 경우 원시 타입으로 선언되어 있어 원시 타입으로 변경하여 해결했습니다.
> 
> ![](src/main/resources/image/wrapper-error-2.png)
>

## 7. 회고
<details>
<summary>정의정</summary>
<div markdown="1">

- **느낀 점**
  - 본 프로젝트에서 BE 팀장으로서 참여하였는데, 전체적인 프로젝트 총괄을 맡으면서 각 팀간 소통과 회의 진행에 있어서 어려움을 느꼈습니다. 
  - 이번 협업 경험을 통해 다른 분야와의 소통 방식에 대해 고민하고, 원활한 소통을 위해 서로의 이해와 배려가 중요하다는 것을 다시 한 번 느낄 수 있었습니다.

- **프로젝트를 하면서 잘했던 점**
  - 매일 Daliy Scrum을 진행하여 현재 프로젝트 진행 상황을 파악하고 효과적으로 일 단위 task를 수행할 수 있었습니다. 
  - 2주 단위 스프린트로 기능을 개발하여 짧은 시간 안에 효율적인 개발이 가능했습니다. 
  - 적극적인 코드 리뷰를 통해 더 나은 코드 작성법에 대해 생각할 수 있었고, 팀원 간 코드를 분석하고 배울 수 있었습니다. 
  - GitHub와 Discord를 웹훅을 통해 연결하여 PR, ISSUE 등 알림 기능을 구현하여 빠른 피드백이 가능했습니다. 
  - REST Docs를 도입하여 테스트 코드를 통한 안정적인 API 문서화를 수행할 수 있었습니다. 

- **프로젝트를 진행하면서 힘들었던 점**
  - FE와 개발 속도를 맞추는데 어려움을 느꼈습니다. 
  - 본 프로젝트는 FE 5명, BE 3명으로, BE가 비교적 적은 인원이었습니다. 
  - 요구사항과 일정에 맞게 개발을 하면서 FE 팀이 API 연결 기간을 충분히 가질 수 있도록 하기 위해 많은 노력이 필요했습니다. 
  - 또한, 서로의 기술에 대해 모르다보니 FE와의 소통에 어려움을 느꼈습니다. 
  - FE와의 협업을 위한 기본적인 지식을 겸비해야겠다는 생각을 했습니다. 

</div>
</details>

<details>
<summary>이주연</summary>
<div markdown="1">

- **느낀 점**
  - 프론트엔드와의 협업 프로젝트는 처음이었는데, 프론트엔드와 소통하며 API를 연결하는 방법을 배울 수 있었던 시간이었습니다.
  - 매일 10시, 17시 데일리 스크럼 시간을 잘 활용해 서로의 상황을 꾸준히 공유하여 계획에 큰 차질없이 프로젝트가 진행될 수 있었던 것 같습니다.
  - 백엔드 팀원들간의 코드 리뷰를 통해 서로의 코드를 공유하고 의견을 나누며 많이 배울 수 있는 시간이었습니다.

- **프로젝트를 하면서 잘했던 점**
    - 매일 10시, 17시 데일리 스크럼 시간을 잘 활용해 서로의 상황을 꾸준히 공유하여 계획에 큰 차질없이 프로젝트가 진행될 수 있었던 것 같습니다.

- **프로젝트를 진행하면서 힘들었던 점**
    - 프론트엔드에 대해 지식이 부족하기 때문에 프론트엔드가 설명해주는 것을 빠르게 이해하고 소통하는데 어려움을 겪었습니다. 이번 프로젝트를 통해 프론트엔드에 대해 조금 알아갈 수 있었던 것 같습니다.

</div>
</details>

<details>
<summary>심재철</summary>
<div markdown="1">

- **배운 점**
  - 프로젝트를 통해 나의 경험 중 하나로, 효과적인 협업을 위해서는 자신의 진행 상황을 솔직하게, 빠르게 전달하는 것이 상당히 중요하다는 것을 깨달았습니다. 투명하게 소통함으로써 팀 전체가 현재 상황을 정확히 파악하고, 문제 발생 시 신속한 대응이 가능하다는 것을 인지했습니다.

- **프로젝트를 하면서 잘했던 점**
  - 프로젝트 진행 중에는 효과적인 협업을 실현하기 위해 서로의 진행 상황을 체계적으로 공유하면서 개발을 진행했습니다.
  - 팀 원 개개인이 빈 시간이 발생할 때 주도적으로 추가 기능이나 프로젝트에 필요한 작업들을 수행하고자 해서 프로젝트 일정에 맞출 수 있었습니다.

- **프로젝트를 진행하면서 힘들었던 점**
  - 프로젝트에서 가장 어려웠던 부분은 팀 구성원이 3명이어서 요구사항에 따른 API 개발을 프로젝트 일정에 맞추는데 빠듯함이 있었습니다.

</div>
</details>

## 8. API 문서

※ Spring REST Docs로 문서화했습니다.

> `index`
> ![](src/main/resources/image/docs/index-docs.png)
>
> `Member API Docs`
> ![](src/main/resources/image/docs/member-docs.png)
>
> `Product API Docs`
> ![](src/main/resources/image/docs/product-docs.png)
>
> `Room API Docs`
> ![](src/main/resources/image/docs/room-docs.png)
>
> `Cart API Docs`
> ![](src/main/resources/image/docs/cart-docs.png)
>
> `Reservation API Docs`
> ![](src/main/resources/image/docs/reservation-docs.png)
>
> `Reservation Product API Docs`
> ![](src/main/resources/image/docs/reservation-product-docs.png)
>
> `Star API Docs`
> ![](src/main/resources/image/docs/star-docs.png)
>
> `Favorite API Docs`
> ![](src/main/resources/image/docs/favorite-docs.png)
