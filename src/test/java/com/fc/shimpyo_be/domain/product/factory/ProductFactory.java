package com.fc.shimpyo_be.domain.product.factory;

import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.product.model.Area;
import com.fc.shimpyo_be.domain.product.entity.Category;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.reservation.entity.PayMethod;
import com.fc.shimpyo_be.domain.reservation.entity.Reservation;
import com.fc.shimpyo_be.domain.reservationproduct.entity.ReservationProduct;
import com.fc.shimpyo_be.domain.room.entity.Room;
import java.sql.Time;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class ProductFactory {

    public static Product createTestProduct() {
        String area = Area.values()[ThreadLocalRandom.current().nextInt(Area.values().length)].toString();
        return Product.builder()
            .name(area +" 숙박")
            .address("서울시"+ area)
            .photoUrl("wjcojaodfjoadsfj,djaofjofjods,fadjofjodsafj")
            .category(Category.values()[ThreadLocalRandom.current().nextInt(Category.values().length)])
            .starAvg(ThreadLocalRandom.current().nextFloat(5))
            .description("좋아요")
            .build();
    }

    public static Room createTestRoom(Product product) {

        int stadard = ThreadLocalRandom.current().nextInt(10);

        return Room.builder()
            .price(ThreadLocalRandom.current().nextInt(100000))
            .description("아주 좁은 방입니다.")
            .product(product)
            .checkIn(Time.valueOf("11:00:00"))
            .checkOut(Time.valueOf("15:00:00"))
            .name(product.getCategory()+" 방")
            .standard(stadard)
            .capacity(stadard+ThreadLocalRandom.current().nextInt(10))
            .build();
    }

    public static ReservationProduct createTestReservationProduct(Room room,Reservation reservation) {

        return ReservationProduct.builder()
            .room(room)
            .reservation(reservation)
            .price(ThreadLocalRandom.current().nextInt(100000))
            .accommodationDate(LocalDate.of(2023,11,22))
            .build();
    }

    public static Reservation createTestReservation(Member member) {

        int stadard = ThreadLocalRandom.current().nextInt(10);

        return Reservation.builder()
            .member(member)
            .payMethod(PayMethod.CASH)
            .build();
    }

    public static Member createTestMember() {
        return Member.builder()
            .name("심재철")
            .email("wocjf0513@naver.com")
            .password("1234")
            .photoUrl("dsklaffjdlsajldfjs")
            .build();
    }



}
