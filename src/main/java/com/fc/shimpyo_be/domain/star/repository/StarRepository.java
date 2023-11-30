package com.fc.shimpyo_be.domain.star.repository;

import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.star.entity.Star;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StarRepository extends JpaRepository<Star, Long> {

    long countByProduct(Product product);
}
