package org.koreait.global.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 모든 사용자 정의 예외 상위 클래스
 *
 */
@Getter @Setter
public class CommonException extends RuntimeException {
    private HttpStatus status;
    private boolean errorCode;
    private Map<String, List<String>> errorMessages;

    public CommonException(String message, HttpStatus status) {
        super(message);
        this.status = Objects.requireNonNullElse(status, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * RestController에서 커맨드 객체 검증 실패시 가공한 에러 메세지 정보
     * @param errorMessages
     * @param status
     */
    public CommonException(Map<String, List<String>> errorMessages, HttpStatus status) {
        this.errorMessages = errorMessages;
        this.status = status;
    }
}
