package com.fc.shimpyo_be.domain.orderproduct.repository;

import com.fc.shimpyo_be.domain.orderproduct.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}
