package org.koreait.global.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

/**
 * 주로 검증 실패시 사용하는 예외
 * 응답 코드 400으로 고정(Bad Request)
 */
public class BadRequestException extends CommonException {
    public BadRequestException() {
        this("BadRequest");
        setErrorCode(true);
    }

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(Map<String, List<String>> messages) {
        super(messages, HttpStatus.BAD_REQUEST);
    }
}
