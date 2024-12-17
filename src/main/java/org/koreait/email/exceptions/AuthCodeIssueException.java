package org.koreait.email.exceptions;

import org.koreait.global.exceptions.BadRequestException;

public class AuthCodeIssueException extends BadRequestException {
    public AuthCodeIssueException() {
        super("Fail.authCode.issue");
        setErrorCode(true);
    }
}
