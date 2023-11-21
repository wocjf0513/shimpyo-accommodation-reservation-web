package com.fc.shimpyo_be.global.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

@Getter
@Setter
@ToString
@Builder
public class ResponseDto<T> {

    private int code;
    private String message;
    private T data;

    public static <T> ResponseDto<T> res(HttpStatus httpStatus, @Nullable T data, String message) {
        return ResponseDto.<T>builder()
            .code(httpStatus.value())
            .message(message)
            .data(data)
            .build();
    }

    public static <T> ResponseDto<T> res(HttpStatus httpStatus, String message) {
        return ResponseDto.<T>builder()
            .code(httpStatus.value())
            .message(message)
            .build();
    }
}
