package com.fc.shimpyo_be.domain.reservation.util;

import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.reservation.dto.response.ReservationInfoResponseDto;
import com.fc.shimpyo_be.domain.reservation.dto.response.SaveReservationResponseDto;
import com.fc.shimpyo_be.domain.reservation.entity.Reservation;
import com.fc.shimpyo_be.domain.reservationproduct.dto.response.ReservationProductResponseDto;
import com.fc.shimpyo_be.domain.reservationproduct.entity.ReservationProduct;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.global.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.List;

public class ReservationMapper {

    public static ReservationInfoResponseDto toReservationInfoResponseDto(ReservationProduct reservationProduct) {
        Reservation reservation = reservationProduct.getReservation();
        Room room = reservationProduct.getRoom();
        Product product = room.getProduct();

        return ReservationInfoResponseDto.builder()
            .reservationId(reservation.getId())
            .reservationProductId(reservationProduct.getId())
            .productId(product.getId())
            .productName(product.getName())
            .productImageUrl(product.getThumbnail())
            .productAddress(product.getAddress().getAddress())
            .productDetailAddress(product.getAddress().getDetailAddress())
            .roomId(room.getId())
            .roomName(room.getName())
            .startDate(DateTimeUtil.toString(reservationProduct.getStartDate()))
            .endDate(DateTimeUtil.toString(reservationProduct.getEndDate()))
            .checkIn(DateTimeUtil.toString(room.getCheckIn()))
            .checkOut(DateTimeUtil.toString(room.getCheckOut()))
            .price(reservationProduct.getPrice())
            .payMethod(reservation.getPayMethod().name())
            .createdAt(DateTimeUtil.toString(reservation.getCreatedAt()))
            .build();
    }

    public static SaveReservationResponseDto toSaveReservationResponseDto(Reservation reservation) {
        List<ReservationProductResponseDto> reservationProductDtos = new ArrayList<>();
        for (ReservationProduct reservationProduct : reservation.getReservationProducts()) {

            Room room = reservationProduct.getRoom();
            Product product = room.getProduct();

            reservationProductDtos.add(
                ReservationProductResponseDto.builder()
                    .productName(product.getName())
                    .roomId(room.getId())
                    .roomName(room.getName())
                    .standard(room.getStandard())
                    .capacity(room.getCapacity())
                    .startDate(DateTimeUtil.toString(reservationProduct.getStartDate()))
                    .endDate(DateTimeUtil.toString(reservationProduct.getEndDate()))
                    .checkIn(DateTimeUtil.toString(room.getCheckIn()))
                    .checkOut(DateTimeUtil.toString(room.getCheckOut()))
                    .visitorName(reservationProduct.getVisitorName())
                    .visitorPhone(reservationProduct.getVisitorPhone())
                    .price(reservationProduct.getPrice())
                    .build()
            );
        }

        return SaveReservationResponseDto.builder()
            .reservationId(reservation.getId())
            .reservationProducts(reservationProductDtos)
            .payMethod(reservation.getPayMethod())
            .totalPrice(reservation.getTotalPrice())
            .createdAt(DateTimeUtil.toString(reservation.getCreatedAt()))
            .build();
    }
}
