package com.fc.shimpyo_be.domain.favorite.exception;

import com.fc.shimpyo_be.global.exception.ApplicationException;
import com.fc.shimpyo_be.global.exception.ErrorCode;

public class FavoriteNotFoundException extends ApplicationException {

    public FavoriteNotFoundException() {
        super(ErrorCode.FAVORITE_NOT_FOUND);
    }
}
