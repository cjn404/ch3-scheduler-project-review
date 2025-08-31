package org.example.ch3schedulerprojectreview.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
public class LoginFilter implements Filter {

    private static final String SESSION_KEY = "SESSION_KEY";    // 상수로 정의. 상수는 모든 글자를 대문자 + 단어 구분은 _가 자바 관례
    private static final String[] WHITE_LIST = {"/", "/auth/signup", "/auth/login"};

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
            if (session == null || session.getAttribute(SESSION_KEY) == null) {
//                throw new RuntimeException("로그인해 주세요.");
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인해 주세요.");
            }
        }
        chain.doFilter(request, response);
    }
    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}
