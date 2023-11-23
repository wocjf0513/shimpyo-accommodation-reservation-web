package com.fc.shimpyo_be.domain.product.repository;


import com.fc.shimpyo_be.domain.product.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;


public interface ProductRepository extends JpaRepository<Product, Long>,
    JpaSpecificationExecutor<Product> {
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

//    @Query("select p from Product p where p.name like %:keyword% or p.address like %:keyword% or p.category like %:keyword%")
//    List<Product> findAllContainingKeyword(@Param("keyword") String keyword, Pageable pageable);

}
