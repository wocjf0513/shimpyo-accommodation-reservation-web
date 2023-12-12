package com.fc.shimpyo_be.domain.favorite.repository;

import com.fc.shimpyo_be.domain.favorite.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteCustomRepository {

    Page<Favorite> findAllByMemberId(long memberId, Pageable pageable);
}
