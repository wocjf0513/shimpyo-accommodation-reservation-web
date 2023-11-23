package com.fc.shimpyo_be.domain.member.repository;

import com.fc.shimpyo_be.domain.member.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

}
