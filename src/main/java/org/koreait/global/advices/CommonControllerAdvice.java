package org.koreait.global.advices;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.koreait.global.annotations.ApplyErrorPage;
import org.koreait.global.entities.SiteConfig;
import org.koreait.global.exceptions.CommonException;
import org.koreait.global.exceptions.scripts.AlertBackException;
import org.koreait.global.exceptions.scripts.AlertException;
import org.koreait.global.exceptions.scripts.AlertRedirectException;
import org.koreait.global.libs.Utils;
import org.koreait.global.services.CodeValueService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice(annotations = ApplyErrorPage.class)
@RequiredArgsConstructor
public class CommonControllerAdvice {
    private final Utils utils;
    private final CodeValueService codeValueService;


    @ExceptionHandler(Exception.class)
    public ModelAndView errorHandler(Exception e, HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 기본 응답 코드 500
        String tpl = "error/error"; // 기본 출력 템플릿
        String message = e.getMessage();

        data.put("method", request.getMethod());
        data.put("path", request.getContextPath() + request.getRequestURI());
        data.put("querystring", request.getQueryString());
        data.put("exception", e);

        if (e instanceof CommonException commonException) {
            status = commonException.getStatus();
            message = commonException.isErrorCode() ? utils.getMessage(message) : message;

            StringBuffer sb = new StringBuffer(2048);
            if (e instanceof AlertException) {
                tpl = "common/_execute_script"; // 스크립트를 실행하기 위한 HTML 템플릿
                sb.append(String.format("alert('%s');", message));
            }

            if (e instanceof AlertBackException backException) {
                String target = backException.getTarget();
                sb.append(String.format("%s.history.back();", target));
            }

            if (e instanceof AlertRedirectException redirectException) {
                String target = redirectException.getTarget();
                String url = redirectException.getUrl();
                sb.append(String.format("%s.location.replace('%s');", target, url));
            }

            if (!sb.isEmpty()) {
                data.put("script", sb.toString());
            }
        }

        data.put("status", status.value());
        data.put("_status", status);
        data.put("message", message);

        SiteConfig siteConfig = Objects.requireNonNullElseGet(codeValueService.get("siteConfig", SiteConfig.class), SiteConfig::new);
        data.put("siteConfig", siteConfig);

        ModelAndView mv = new ModelAndView();
        mv.setStatus(status);
        mv.addAllObjects(data);
        mv.setViewName(tpl);
        return mv;
    }
}
