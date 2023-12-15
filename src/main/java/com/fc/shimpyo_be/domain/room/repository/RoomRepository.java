package com.fc.shimpyo_be.domain.room.repository;

import com.fc.shimpyo_be.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository
    extends JpaRepository<Room, Long>, RoomRepositoryCustom {

    List<Room> findByCode(long code);

    @Query("select r.id from Room r where r.code = :code")
    List<Long> findIdsByCode(@Param("code") Long code);
}
