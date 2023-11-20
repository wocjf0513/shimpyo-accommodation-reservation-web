package com.fc.shimpyo_be.domain.order.repository;

import com.fc.shimpyo_be.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
