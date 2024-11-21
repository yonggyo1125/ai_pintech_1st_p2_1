package org.koreait.global.exceptions.scripts;

import org.koreait.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

/**
 * 예외 발생하면 자바스크립트 alert('메세지'); 로 실행
 */
public class AlertException extends CommonException {
    public AlertException(String message) {
        this(message, null);
    }

    public AlertException(String message, HttpStatus status) {
        super(message, status);
    }
}
