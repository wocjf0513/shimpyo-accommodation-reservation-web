package com.fc.shimpyo_be.domain.reservation.service;

import com.fc.shimpyo_be.domain.cart.service.CartService;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.member.service.MemberService;
import com.fc.shimpyo_be.domain.product.exception.RoomNotFoundException;
import com.fc.shimpyo_be.domain.reservation.dto.ValidateReservationResultDto;
import com.fc.shimpyo_be.domain.reservation.dto.request.ReleaseRoomItemRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.request.ReleaseRoomsRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.request.SaveReservationRequestDto;
import com.fc.shimpyo_be.domain.reservation.dto.response.ReservationInfoResponseDto;
import com.fc.shimpyo_be.domain.reservation.dto.response.SaveReservationResponseDto;
import com.fc.shimpyo_be.domain.reservation.entity.Reservation;
import com.fc.shimpyo_be.domain.reservation.repository.ReservationRepository;
import com.fc.shimpyo_be.domain.reservation.util.ReservationMapper;
import com.fc.shimpyo_be.domain.reservationproduct.dto.request.ReservationProductRequestDto;
import com.fc.shimpyo_be.domain.reservationproduct.entity.ReservationProduct;
import com.fc.shimpyo_be.domain.reservationproduct.repository.ReservationProductRepository;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
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
import java.time.ZoneId;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationProductRepository reservationProductRepository;
    private final MemberService memberService;
    private final RoomRepository roomRepository;
    private final CartService cartService;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String REDIS_ROOM_KEY_FORMAT = "roomId:%d:%s";

    @Transactional
    public SaveReservationResponseDto saveReservation(
        Long memberId, SaveReservationRequestDto request, Map<Long, List<String>> reservationMap
    ) {
        log.debug("{} ::: {}", getClass().getSimpleName(), "saveReservation");

        // 회원 엔티티 조회
        Member member = memberService.getMemberById(memberId);

        // 객실 엔티티 조회 후, 예약 숙소 리스트 생성
        List<ReservationProduct> reservationProducts = new ArrayList<>();
        for (ReservationProductRequestDto reservationProductDto : request.reservationProducts()) {

            Room room = roomRepository.findById(reservationProductDto.roomId())
                .orElseThrow(RoomNotFoundException::new);

            // 장바구니 아이템 삭제
            if (reservationProductDto.cartId() > 0) {
                cartService.deleteCart(member.getId(), reservationProductDto.cartId());
            }

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

            confirmReservationProduct(reservationMap.get(room.getId()), reservationProductDto.endDate());
        }

        // 예약 저장
        Reservation reservation = reservationRepository.save(
            Reservation.builder()
                .member(member)
                .reservationProducts(reservationProducts)
                .payMethod(request.payMethod())
                .totalPrice(request.totalPrice())
                .build()
        );

        return ReservationMapper.toSaveReservationResponseDto(reservation);
    }

    @Transactional(readOnly = true)
    public Page<ReservationInfoResponseDto> getReservationInfoList(Long memberId, Pageable pageable) {
        log.debug("{} ::: {}", getClass().getSimpleName(), "getReservationInfoList");

        List<Long> reservationIds = reservationRepository.findIdsByMemberId(memberId);

        return reservationProductRepository
            .findAllInReservationIds(reservationIds, pageable)
            .map(ReservationMapper::toReservationInfoResponseDto);
    }

    public ValidateReservationResultDto validate(Long memberId, List<ReservationProductRequestDto> reservationProducts) {
        log.debug("{} ::: {}", getClass().getSimpleName(), "validate");

        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();

        boolean isValid = true;
        List<Long> invalidRoomIds = new ArrayList<>();
        Map<Long, List<String>> confirmMap = new HashMap<>();
        String memberIdValue = String.valueOf(memberId);

        for (ReservationProductRequestDto reservationProduct : reservationProducts) {

            LocalDate targetDate = DateTimeUtil.toLocalDate(reservationProduct.startDate());
            LocalDate endDate = DateTimeUtil.toLocalDate(reservationProduct.endDate());
            Long roomId = reservationProduct.roomId();
            List<String> keys = new LinkedList<>();

            while (targetDate.isBefore(endDate)) {
                keys.add(String.format(REDIS_ROOM_KEY_FORMAT, roomId, targetDate));

                targetDate = targetDate.plusDays(1);
            }

            confirmMap.put(roomId, keys);

            List<Object> values = opsForValue.multiGet(keys);

            if (ObjectUtils.isEmpty(values)) {
                isValid = false;
                invalidRoomIds.add(roomId);
                continue;
            }

            for (Object value : values) {
                if (Objects.isNull(value) || !memberIdValue.equals(value)) {
                    isValid = false;
                    invalidRoomIds.add(roomId);
                    break;
                }
            }
        }

        return ValidateReservationResultDto.builder()
            .isAvailable(isValid)
            .unavailableIds(invalidRoomIds)
            .confirmMap(confirmMap)
            .build();
    }

    public void releaseRooms(Long memberId, ReleaseRoomsRequestDto request) {
        log.debug("{} ::: {}", getClass().getSimpleName(), "releaseRooms");

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

    private void confirmReservationProduct(List<String> reservationKeys, String endDate) {
        log.debug("{} ::: {}", getClass().getSimpleName(), "confirmReservationProduct");

        Date expireDate = convertLocalDateToDate(DateTimeUtil.toLocalDate(endDate));
        for (String key : reservationKeys) {
            redisTemplate.expireAt(key, expireDate);
        }
    }

    private Date convertLocalDateToDate(LocalDate localDate) {
        return Date.from(
            localDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        );
    }
}
