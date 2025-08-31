package org.example.ch3schedulerprojectreview.common.constants.auth;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionKey {

//    private SessionKey(){}

    public static final String SESSION_KEY = "SESSION_KEY";    // 상수로 정의. 상수는 모든 글자를 대문자 + 단어 구분은 _가 자바 관례
}
