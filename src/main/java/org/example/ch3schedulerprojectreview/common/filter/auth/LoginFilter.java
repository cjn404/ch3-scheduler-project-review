package org.example.ch3schedulerprojectreview.common.filter.auth;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.example.ch3schedulerprojectreview.common.constants.auth.SessionKey;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
public class LoginFilter implements Filter {

    private static final String[] WHITE_LIST = {"/", "/users/signup", "/users/login"};

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        log.info("로그인 필터 로직 실행");

        if (!isWhiteList(requestURI)) {

            HttpSession session = httpRequest.getSession(false);
            if (session == null || session.getAttribute(SessionKey.SESSION_KEY) == null) {
//                throw new RuntimeException("로그인해 주세요.");
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인해 주세요.");
                return;    // 세션이 없거나 인증되지 않은 경우, doFilter까지 가지않고 요청 처리 중단
            } else {
                session.setMaxInactiveInterval(30 * 60);    // 모든 유효 세션에 대한 세션 만료 시간 갱신
            }
        }
        chain.doFilter(request, response);
    }
    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}
