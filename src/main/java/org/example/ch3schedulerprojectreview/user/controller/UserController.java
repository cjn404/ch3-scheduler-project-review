package org.example.ch3schedulerprojectreview.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ch3schedulerprojectreview.common.constants.auth.SessionKey;
import org.example.ch3schedulerprojectreview.user.dto.*;
import org.example.ch3schedulerprojectreview.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController    // REST API 컨트롤러임을 명시. JSON 형태 응답을 자동 반환
@RequiredArgsConstructor    // final 필드를 생성자로 주입하도록 생성자 자동 생성
@RequestMapping("/users")    // 공통 URL prefix
public class UserController {

    private final UserService userService;    // 서비스 계층 주입

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(
            @Valid @RequestBody UserRequest request    // @RequestBody -> JSON 바인딩, JSON 바디-> UserRequest 객체로 바인딩. @Valid로 입력값 검증
    ) {
        UserResponse response = userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @Valid @RequestBody UserLoginRequest loginRequest,
            HttpServletRequest servletRequest    // HttpServletRequest 통해 세션 관리
//            @SessionAttribute(name = SessionKey.SESSION_KEY) Long sessionUserId
    ) {
        UserResponse response = userService.login(loginRequest);    // Service에서 로그인 처리 후 UserResponse 반환

        HttpSession session = servletRequest.getSession();    // 새 세션 생성 또는 기존 세션 반환
        session.setAttribute(SessionKey.SESSION_KEY, response.getUserId());    // 세션에 유저ID 저장 (로그인 상태 유지)
        /** setAttribute(String key, Object value)
         * 세션 안에 데이터를 저장하는 메서드
         * key: 세션 안에서 데이터를 꺼낼 때 사용하는 이름
         * value: 실제 저장할 값 -> 로그인 후 DB에서 찾아온 유저ID
         *                       세션 저장 시 서버에서 가져온 유저 정보가 필요하므로 Response DTO를 사용
         */
        return ResponseEntity.ok(response);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest servletRequest) {
        // 로그인하지 않으면 HttpSession -> Null로 반환
        HttpSession session = servletRequest.getSession(false);    // 기존 세션 가져오기, 없으면 null 반환. false -> 세션이 없으면 새로 생성하지 않음
        // 세션 존재 시 = 로그인이 된 경우
        // 해당 세션에 저장된 유저ID 삭제
        if (session != null)
            session.invalidate();
        return ResponseEntity.noContent().build();
    }

    // 회원탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<Void> withdraw(
            HttpServletRequest servletRequest,
            @RequestBody UserWithdrawRequest withdrawRequest) {
        // 로그인하지 않으면 HttpSession -> Null로 반환
        HttpSession session = servletRequest.getSession(false);
        Long sessionUserId = (Long) session.getAttribute(SessionKey.SESSION_KEY);    // session.getAttribute()는 Object 타입을 반환 -> Long으로 타입 캐스팅 필요

        userService.withdraw(sessionUserId, withdrawRequest);    // DB 삭제
        session.invalidate();    // 해당 세션(데이터) 삭제
        return ResponseEntity.noContent().build();
    }

    // 조회
    @GetMapping("/me")
    public ResponseEntity<UserResponse> findMe(
            HttpServletRequest servletRequest
    ) {
        HttpSession session = servletRequest.getSession(false);
        // 세션에서 사용자 정보 가져오기
        Long sessionUserId = (Long) session.getAttribute(SessionKey.SESSION_KEY);

        UserResponse response = userService.findMe(sessionUserId);

        return ResponseEntity.ok(response);
    }

    // 수정
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updatedMe(
            HttpServletRequest servletRequest,
            @RequestBody UserUpdateRequest updateRequest
    ) {
        HttpSession session = servletRequest.getSession(false);
        // 세션에서 사용자 정보 가져오기
        Long sessionUserId = (Long) session.getAttribute(SessionKey.SESSION_KEY);

        // 서비스 호출
        UserResponse updatedUser = userService.updateMe(sessionUserId, updateRequest);

        return ResponseEntity.ok(updatedUser);
    }
}
