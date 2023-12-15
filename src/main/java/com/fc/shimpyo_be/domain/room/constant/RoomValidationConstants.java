package com.fc.shimpyo_be.domain.room.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomValidationConstants {

    // validation constraint value
    public static final int ROOM_REQ_MIN_SIZE = 1;
    public static final int ROOM_REQ_MAX_SIZE = 3;
    public static final long ROOMID_MIN_VALUE = 1;

    // validation message
    public static final String ROOM_REQ_SIZE_MESSAGE = "최소 1개, 최대 3개의 객실 식별자 정보가 필요합니다.";
    public static final String ROOMID_MIN_MESSAGE = "객실 식별자는 최소 1 이상이어야 합니다.";
}
