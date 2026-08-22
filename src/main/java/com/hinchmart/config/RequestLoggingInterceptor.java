package com.hinchmart.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingInterceptor.class);
    private static final String START_TIME_ATTR = "HINCHMART_REQUEST_START_TIME";

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        request.setAttribute(START_TIME_ATTR, System.currentTimeMillis());

        String uri = request.getRequestURI();
        if (isStaticOrSwagger(uri)) {
            return true;
        }

        String method = request.getMethod();
        String queryString = request.getQueryString() != null ? "?" + request.getQueryString() : "";
        String user = getCurrentUserDescription();

        log.info(">>> [HTTP IN] {} {}{} | Auth: {}", method, uri, queryString, user);
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex) {
        String uri = request.getRequestURI();
        if (isStaticOrSwagger(uri)) {
            return;
        }

        Long startTime = (Long) request.getAttribute(START_TIME_ATTR);
        long duration = (startTime != null) ? (System.currentTimeMillis() - startTime) : 0;
        int status = response.getStatus();
        String method = request.getMethod();

        if (status >= 400) {
            log.warn("<<< [HTTP OUT] {} {} | Status: {} | Duration: {}ms", method, uri, status, duration);
        } else {
            log.info("<<< [HTTP OUT] {} {} | Status: {} | Duration: {}ms", method, uri, status, duration);
        }
    }

    private String getCurrentUserDescription() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return auth.getName() + " " + auth.getAuthorities();
        }
        return "Anonymous/Guest";
    }

    private boolean isStaticOrSwagger(String uri) {
        return uri.startsWith("/swagger-ui") || uri.startsWith("/v3/api-docs") || uri.startsWith("/favicon.ico");
    }
}
