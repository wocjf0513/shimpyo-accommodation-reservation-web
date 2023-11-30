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

- 👩🏻‍💻 [정의정](https://github.com/JeongUijeong)
- 👩🏻‍💻 [심재철](https://github.com/wocjf0513)
- 👩🏻‍💻 [이주연](https://github.com/jo0oy)

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