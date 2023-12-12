package com.fc.shimpyo_be.domain.favorite.repository;

import com.fc.shimpyo_be.domain.favorite.entity.Favorite;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.product.entity.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByMemberAndProduct(Member member, Product product);
}
