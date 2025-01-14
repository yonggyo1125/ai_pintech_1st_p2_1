package org.koreait.board.exceptions;

import org.koreait.global.exceptions.BadRequestException;

public class GuestPasswordCheckException extends BadRequestException {
    public GuestPasswordCheckException() {
        super("Required.guestPassword");
        setErrorCode(true);
    }
}
