package org.koreait.email.exceptions;

import org.koreait.global.exceptions.BadRequestException;

public class AuthCodeMismatchException extends BadRequestException {
    public AuthCodeMismatchException() {
        super("Mismatch.authCode");
        setErrorCode(true);
    }
}
