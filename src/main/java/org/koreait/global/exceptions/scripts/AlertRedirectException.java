package org.koreait.global.exceptions.scripts;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

@Getter @Setter
public class AlertRedirectException extends AlertException {
    private String target;
    private String url; // 이동할 경로

    public AlertRedirectException(String message, String url, HttpStatus status, String target) {
        super(message, status);
        target = StringUtils.hasText(target) ? target : "self";

        this.url = url;
        this.target = target;
    }

    public AlertRedirectException(String message, String url, HttpStatus status) {
        this(message, url, status, null);
    }

    public AlertRedirectException(String message, String url) {
        this(message, url, null);
    }
}
