package com.fc.shimpyo_be.domain.member.repository;

import com.fc.shimpyo_be.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
