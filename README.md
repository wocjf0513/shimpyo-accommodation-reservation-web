# Shimpyo_BE : 숙박 예약 서비스

2023-11-20 ~ 2023-12-01

---

## 📌 목차

- [멤버](#멤버)
- [설정](#설정)
- [설계](#설계)
  - [아키텍처](#아키텍처)
  - [DB 설계](#DB-설계)
  - [API 설계](#API-설계)
- [CI/CD](#CICD)
- [API 문서](#API-문서)

---

## 멤버

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
        Backend
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/wocjf0513" target="_blank">
        심재철<br />
        Backend
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/jo0oy" target="_blank">
        이주연<br />
        Backend
      </a>
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
        <img src="https://github.com/Shimpyo-House/Shimpyo_FE/assets/104253583/cab3f3cc-ccb0-46cc-8b36-bf645b5c4086" alt="정지오 프로필" />
      </a>
    </td>
    <td align="center" width="150px">
      <a href="https://github.com/Yamyam-code" target="_blank">
        <img src="https://github.com/KDT1-FE/Y_FE_Toy1/assets/39702832/58fb577d-9f8c-4679-bca1-8ff15ca84f6b" alt="백상원 프로필" />
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
        Frontend
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/jiohjung98" target="_blank">
        정지오<br />
        Frontend
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/Yamyam-code" target="_blank">
        백상원<br />
        Frontend
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/wkdtnqls0506" target="_blank">
        장수빈<br />
        Frontend
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/seacrab808" target="_blank">
        소유나<br />
        Frontend
      </a>
    </td>
  </table>
<br>


---

## 설정

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
- `applicaion-local.yaml`, `application-prod.yaml`, `.env` 파일은 LMS에서 확인하실 수 있습니다!

---

## 설계

### 아키텍처

> ![](src/main/resources/image/architecture.png)

### DB 설계
`ERD`
> ![](src/main/resources/image/erd.png)

### API 설계

[Spring REST Docs](#API-문서)를 통해 확인하실 수 있습니다.

---

## CI/CD

### CI

> ![](src/main/resources/image/ci.png)

### CD

> ![](src/main/resources/image/cd.png)

---

## API 문서

※ Spring REST Docs로 문서화했습니다.

> `index`
> ![](src/main/resources/image/index-docs.png)
>
> `Member API Docs`
> ![](src/main/resources/image/member-docs.png)
>
> `Product API Docs`
> ![](src/main/resources/image/product-docs.png)
>
> `Cart API Docs`
> ![](src/main/resources/image/cart-docs.png)
>
> `Reservation API Docs`
> ![](src/main/resources/image/reservation-docs.png)
>
> `Reservation Product API Docs`
> ![](src/main/resources/image/reservation-product-docs.png)
>
> `Star API Docs`
> ![](src/main/resources/image/star-docs.png)
>  