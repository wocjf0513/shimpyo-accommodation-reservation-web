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

    public static ReservationInfoResponseDto from(ReservationProduct reservationProduct) {
        Reservation reservation = reservationProduct.getReservation();
        Room room = reservationProduct.getRoom();
        Product product = room.getProduct();

        return new ReservationInfoResponseDto(
            reservation.getId(),
            reservationProduct.getId(),
            product.getId(),
            product.getName(),
            product.getThumbnail(),
            product.getAddress().getAddress(),
            product.getAddress().getDetailAddress(),
            room.getId(),
            room.getName(),
            DateTimeUtil.toString(reservationProduct.getStartDate()),
            DateTimeUtil.toString(reservationProduct.getEndDate()),
            DateTimeUtil.toString(room.getCheckIn()),
            DateTimeUtil.toString(room.getCheckOut()),
            reservationProduct.getPrice(),
            reservation.getPayMethod().name()
        );
    }

    public static SaveReservationResponseDto from(Reservation reservation) {
        List<ReservationProductResponseDto> reservationProductDtos = new ArrayList<>();
        for (ReservationProduct reservationProduct : reservation.getReservationProducts()) {
            Room room = reservationProduct.getRoom();
            Product product = room.getProduct();
            reservationProductDtos.add(
                new ReservationProductResponseDto(
                    product.getName(),
                    room.getId(),
                    room.getName(),
                    room.getStandard(),
                    room.getCapacity(),
                    DateTimeUtil.toString(reservationProduct.getStartDate()),
                    DateTimeUtil.toString(reservationProduct.getEndDate()),
                    DateTimeUtil.toString(room.getCheckIn()),
                    DateTimeUtil.toString(room.getCheckOut()),
                    reservationProduct.getVisitorName(),
                    reservationProduct.getVisitorPhone(),
                    reservationProduct.getPrice()
                )
            );
        }

        return new SaveReservationResponseDto(
            reservation.getId(),
            reservationProductDtos,
            reservation.getPayMethod(),
            reservation.getTotalPrice(),
            DateTimeUtil.toString(reservation.getCreatedAt())
        );
    }
}
