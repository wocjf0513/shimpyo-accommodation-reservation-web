package com.fc.shimpyo_be.domain.product.service;

import com.fc.shimpyo_be.domain.product.dto.response.ProductDetailsResponse;
import com.fc.shimpyo_be.domain.product.dto.response.ProductResponse;
import com.fc.shimpyo_be.domain.product.exception.ProductNotFoundException;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.product.util.ProductMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getAllProducts(String keyword, final Pageable pageable) {
        List<ProductResponse> allProducts = Optional.of(productRepository.findAllContainingKeyword(keyword, pageable)).orElseThrow().getContent()
            .stream().map(ProductMapper::from).toList();

        if(allProducts.isEmpty()) {
            throw new ProductNotFoundException();
        }
        return allProducts;
    }

//    public List<ProductDetailsResponse> getProductDetails() {
//        productRepository.findById()
//    }

}
