package com.fc.shimpyo_be.domain.cart.dto.request;

import com.fc.shimpyo_be.global.util.DateTimeUtil;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

public record CartCreateRequest(@NotNull Long roomId,

                                @Pattern(regexp = DateTimeUtil.LOCAL_DATE_REGEX_PATTERN, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25)") String startDate,
                                @Pattern(regexp = DateTimeUtil.LOCAL_DATE_REGEX_PATTERN, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25)") String endDate,
                                @NotNull
                                Long price) {

    @Builder
    public CartCreateRequest(Long roomId, String startDate, String endDate, Long price) {
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
    }
}
