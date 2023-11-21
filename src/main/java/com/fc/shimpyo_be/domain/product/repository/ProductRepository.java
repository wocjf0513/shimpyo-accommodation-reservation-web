package com.fc.shimpyo_be.domain.product.repository;


import com.fc.shimpyo_be.domain.product.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProductRepository extends JpaRepository<Product, Long> {

        @Query("select p from Product p where p.name like %:keyword% or p.address like %:keyword%")
        Page<Product> findAllContainingKeyword(@Param("keyword") String keyword, Pageable pageable);

}
