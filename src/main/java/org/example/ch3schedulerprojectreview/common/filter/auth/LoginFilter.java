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
/**
 * 자동으로 private static final Logger log = LoggerFactory.getLogger(LoginFilter.class); 생성
 * 로그를 찍을 때 사용 가능
 */
public class LoginFilter implements Filter {    // 서블릿 필터는 요청(Request)이 컨트롤러에 도달하기 전에 가로채서 처리 가능. 로그인 여부 체크 인증 필터

    // 필터를 거치지 않고 모두 허용할 URL 지정
    private static final String[] WHITE_LIST = {"/", "/users/signup", "/users/login"};

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        // 형변환해서 HTTP 기능 사용
        // HttpServletResponse에만 있는 메서드. ServletResponse에는 이런 메서드가 없음
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();    // 요청 URI를 문자열로 반환

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 로그 찍기
        // 요청이 필터를 통과했는지 확인 가능
        log.info("로그인 필터 로직 실행");

        if (!isWhiteList(requestURI)) {

            HttpSession session = httpRequest.getSession(false);
            if (session == null || session.getAttribute(SessionKey.SESSION_KEY) == null) {
//                throw new RuntimeException("로그인해 주세요.");
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인해 주세요.");
                return;    // 세션이 없거나 인증되지 않은 경우, doFilter까지 가지않고 요청 처리 중단
//            } else {
//                session.setMaxInactiveInterval(30 * 60);    // 모든 유효 세션에 대한 세션 만료 시간 갱신 * -> 딱히 필요가 없다. 이유) 디폴트값으로 만료시간이 연장된다.
            }
        }
        chain.doFilter(request, response);
    }
    // 요청 URI가 화이트리스트에 포함되는지 확인. 단순 패턴 매칭(문자열 배열 비교)
    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}
