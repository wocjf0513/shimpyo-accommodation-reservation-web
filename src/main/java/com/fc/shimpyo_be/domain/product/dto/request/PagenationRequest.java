package com.fc.shimpyo_be.domain.product.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PagenationRequest {

    private final Long DEFAULT_PAGE_SIZE = 10L;
    private final Long DEFAULT_PAGE_NUM = 0L;

    private Long pageSize;
    private Long pageNo;
    private String itemToSort;
    private Direction sortDirection;

}
