package com.fc.shimpyo_be.domain.cart.repository;


import com.fc.shimpyo_be.domain.cart.entity.Cart;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<List<Cart>> findByMemberId(Long memberId);

}
