package com.fc.shimpyo_be.domain.star.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException() {
        super("존재하지 않는 엔티티입니다.");
    }

    public EntityNotFoundException(String entityType) {
        super("존재하지 않는 " + entityType + " 엔티티입니다.");
    }
}
