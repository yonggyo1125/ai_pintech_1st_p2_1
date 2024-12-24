package org.koreait.global.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.koreait.global.entities.SiteConfig;
import org.koreait.global.services.CodeValueService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CommonInterceptor implements HandlerInterceptor {

    private final CodeValueService codeValueService;

    private List<String> excludeExtensions = List.of(
            ".css",
            ".js",
            ".png",
            ".gif",
            ".jpg",
            ".jpeg",
            ".pdf",
            ".txt"
    );

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        setSiteConfig(modelAndView, request);

    }


    /* 사이트 설정 */
    private void setSiteConfig(ModelAndView mv, HttpServletRequest request) {
        String uri = request.getRequestURI().toLowerCase();
        boolean matched = excludeExtensions.stream().anyMatch(uri::contains);
        if (!matched) {
            return;
        }

        SiteConfig config = Objects.requireNonNullElseGet(codeValueService.get("siteConfig", SiteConfig.class), SiteConfig::new);

        mv.addObject("siteConfig", config);
    }
}
