package com.fc.shimpyo_be.domain.product.repository;

import com.fc.shimpyo_be.domain.product.dto.request.SearchKeywordRequest;
import com.fc.shimpyo_be.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductCustomRepository {

    Page<Product> findAllBySearchKeywordRequest(SearchKeywordRequest searchKeywordRequest, Pageable pageable);

}
