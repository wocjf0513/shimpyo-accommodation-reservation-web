package com.fc.shimpyo_be.domain.room.repository;

import com.fc.shimpyo_be.domain.room.entity.Room;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByCode(long code);

}
