package com.fc.shimpyo_be.domain.room.repository;

import com.fc.shimpyo_be.domain.room.entity.Room;

import java.util.List;

public interface RoomRepositoryCustom {

    List<Room> findAllInIdsWithProductAndPrice(List<Long> roomIds);
}
