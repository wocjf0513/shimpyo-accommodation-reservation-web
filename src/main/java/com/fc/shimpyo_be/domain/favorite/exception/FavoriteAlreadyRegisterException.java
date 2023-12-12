package com.fc.shimpyo_be.domain.favorite.exception;

import com.fc.shimpyo_be.global.exception.ApplicationException;
import com.fc.shimpyo_be.global.exception.ErrorCode;

public class FavoriteAlreadyRegisterException extends ApplicationException {

    public FavoriteAlreadyRegisterException() {
        super(ErrorCode.FAVORITE_ALREADY_REGISTER);
    }
}
