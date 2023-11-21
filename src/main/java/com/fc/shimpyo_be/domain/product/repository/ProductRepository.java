package com.fc.shimpyo_be.domain.product.repository;


import com.fc.shimpyo_be.domain.product.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {

        Page<Product> findAll(Pageable pageable);

}
