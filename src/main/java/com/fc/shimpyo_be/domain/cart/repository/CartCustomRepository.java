package com.fc.shimpyo_be.domain.cart.repository;

import com.fc.shimpyo_be.domain.cart.dto.request.CartCreateRequest;
import java.time.LocalDate;

public interface CartCustomRepository {
    Long countByRoomCodeAndMemberIdContainsDate (CartCreateRequest cartCreateRequest, Long memberId);
}

