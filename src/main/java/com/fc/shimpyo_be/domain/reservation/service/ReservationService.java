package com.fc.shimpyo_be.domain.reservation.service;

import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.member.exception.MemberNotFoundException;
import com.fc.shimpyo_be.domain.member.repository.MemberRepository;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.exception.RoomNotFoundException;
import com.fc.shimpyo_be.domain.reservation.dto.request.ReleaseRoomItemRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.request.ReleaseRoomsRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.request.SaveReservationRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.response.ReservationInfoResponseDto;
import com.fc.shimpyo_be.domain.reservation.dto.response.SaveReservationResponseDto;
import com.fc.shimpyo_be.domain.reservation.dto.response.ValidationResultResponseDto;
import com.fc.shimpyo_be.domain.reservation.entity.Reservation;
import com.fc.shimpyo_be.domain.reservation.exception.InvalidRequestException;
import com.fc.shimpyo_be.domain.reservation.repository.ReservationRepository;
import com.fc.shimpyo_be.domain.reservationproduct.dto.request.ReservationProductRequestDto;
import com.fc.shimpyo_be.domain.reservationproduct.entity.ReservationProduct;
import com.fc.shimpyo_be.domain.reservationproduct.repository.ReservationProductRepository;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import com.fc.shimpyo_be.global.exception.ErrorCode;
import com.fc.shimpyo_be.global.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationProductRepository reservationProductRepository;
    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String REDIS_ROOM_KEY_FORMAT = "roomId:%d:%s";

    @Transactional
    public SaveReservationResponseDto saveReservation(Long memberId, SaveReservationRequestDto request) {
        log.info("{} ::: {}", getClass().getSimpleName(), "saveReservation");

        // 회원 엔티티 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        // 객실 엔티티 조회 후, 예약 상품 리스트 생성
        List<ReservationProduct> reservationProducts = new ArrayList<>();
        for (ReservationProductRequestDto reservationProductDto : request.reservationProducts()) {
            Room room = roomRepository.findById(reservationProductDto.roomId())
                .orElseThrow(RoomNotFoundException::new);

            reservationProducts.add(
                ReservationProduct.builder()
                    .room(room)
                    .startDate(DateTimeUtil.toLocalDate(reservationProductDto.startDate()))
                    .endDate(DateTimeUtil.toLocalDate(reservationProductDto.endDate()))
                    .visitorName(reservationProductDto.visitorName())
                    .visitorPhone(reservationProductDto.visitorPhone())
                    .price(reservationProductDto.price())
                    .build()
            );
        }

        // 예약 저장
        Long reservationId
            = reservationRepository.save(
                Reservation.builder()
                    .member(member)
                    .reservationProducts(reservationProducts)
                    .payMethod(request.payMethod())
                    .totalPrice(request.totalPrice())
                    .build()).getId();

        return new SaveReservationResponseDto(reservationId, request);
    }

    @Transactional(readOnly = true)
    public Page<ReservationInfoResponseDto> getReservationInfoList(Long memberId, Pageable pageable) {
        log.info("{} ::: {}", getClass().getSimpleName(), "getReservationInfoList");

        List<Long> reservationIds = reservationRepository.findIdsByMemberId(memberId);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        return reservationProductRepository
            .findAllInReservationIds(reservationIds, pageable)
            .map(
                reservationProduct -> {
                    Reservation reservation = reservationProduct.getReservation();
                    Room room = reservationProduct.getRoom();
                    Product product = room.getProduct();

                    return new ReservationInfoResponseDto(
                        reservation.getId(),
                        reservationProduct.getId(),
                        product.getId(),
                        product.getName(),
                        product.getThumbnail(),
                        product.getAddress().getAddress() + " " + product.getAddress().getDetailAddress(),
                        room.getId(),
                        room.getName(),
                        dateFormatter.format(reservationProduct.getStartDate()),
                        dateFormatter.format(reservationProduct.getEndDate()),
                        timeFormatter.format(room.getCheckIn()),
                        timeFormatter.format(room.getCheckOut()),
                        reservationProduct.getPrice(),
                        reservation.getPayMethod().name()
                    );
                }
            );
    }

    public ValidationResultResponseDto validate(Long memberId, List<ReservationProductRequestDto> reservationProducts) {
        log.info("{} ::: {}", getClass().getSimpleName(), "validate");

        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();

        boolean isValid = true;
        List<Long> invalidRoomIds = new ArrayList<>();
        for (ReservationProductRequestDto reservationProduct : reservationProducts) {
            LocalDate targetDate = DateTimeUtil.toLocalDate(reservationProduct.startDate());
            LocalDate endDate = DateTimeUtil.toLocalDate(reservationProduct.endDate());

            List<String> keys = new LinkedList<>();
            while(targetDate.isBefore(endDate)) {
                keys.add(String.format(REDIS_ROOM_KEY_FORMAT, reservationProduct.roomId(), targetDate));
                targetDate = targetDate.plusDays(1);
            }

            List<Object> values = opsForValue.multiGet(keys);
            String memberIdValue = String.valueOf(memberId);

            if(ObjectUtils.isEmpty(values)) {
                throw new InvalidRequestException(ErrorCode.INVALID_RESERVATION_REQUEST);
            }

            for (Object value : values) {
                if(!memberIdValue.equals(value)) {
                    isValid = false;
                    invalidRoomIds.add(Long.valueOf((String) value));
                    break;
                }
            }
        }

        return new ValidationResultResponseDto(isValid, invalidRoomIds);
    }

    public void releaseRooms(Long memberId, ReleaseRoomsRequestDto request) {
        log.info("{} ::: {}", getClass().getSimpleName(), "releaseRooms");

        String memberIdValue = String.valueOf(memberId);
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();

        for (ReleaseRoomItemRequestDto roomItem : request.rooms()) {
            LocalDate targetDate = DateTimeUtil.toLocalDate(roomItem.startDate());
            LocalDate endDate = DateTimeUtil.toLocalDate(roomItem.endDate());

            List<String> deleteKeys = new ArrayList<>();
            while (targetDate.isBefore(endDate)) {
                String key = String.format(REDIS_ROOM_KEY_FORMAT, roomItem.roomId(), targetDate);

                if (Objects.equals(opsForValue.get(key), memberIdValue)) {
                    deleteKeys.add(key);
                }

                targetDate = targetDate.plusDays(1);
            }

            redisTemplate.delete(deleteKeys);
        }
    }
}
