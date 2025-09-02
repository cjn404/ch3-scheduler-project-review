package org.example.ch3schedulerprojectreview.common.filter.auth;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration    // 스프링 설정 클래스임을 나타냄
public class FilterConfig {

    @Bean    // @Bean으로 스프링에 등록. 메서드 이름이 빈 이름이 됨
    public FilterRegistrationBean<LoginFilter> loginFilter() {
        // FilterRegistrationBean 객체 생성
        FilterRegistrationBean<LoginFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        // 실제 실행될 필터 객체 지정
        filterRegistrationBean.setFilter(new LoginFilter());
        // 모든 요청 URL("/*")에 필터 적용
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);    // 필터 순서. 가장 높은 우선순위 다음

        return filterRegistrationBean;
    }
}
