package org.koreait.global.exceptions.scripts;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

/**
 * 예외가 발생하면 alert('메세지');  타겟.history.back();
 */
@Getter @Setter
public class AlertBackException extends AlertException {
    private String target;

    public AlertBackException(String message, HttpStatus status, String target) {
        super(message, status);

        target = StringUtils.hasText(target) ? target : "self"; // 기본값은 현재 창에 이동
        this.target = target;

    }

    public AlertBackException(String message, HttpStatus status) {
        this(message, status, null);
    }

    public AlertBackException(String message) {
        this(message, null);
    }
}

