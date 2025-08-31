package org.example.ch3schedulerprojectreview.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ch3schedulerprojectreview.common.constants.auth.SessionKey;
import org.example.ch3schedulerprojectreview.common.exception.custom.UnauthorizedException;
import org.example.ch3schedulerprojectreview.user.dto.*;
import org.example.ch3schedulerprojectreview.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(
            @Valid @RequestBody UserRequest request
    ) {
        UserResponse response = userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @Valid @RequestBody UserLoginRequest loginRequest,
            HttpServletRequest servletRequest
    ) {
        UserResponse response = userService.login(loginRequest);

        // 신규 세션 생성
        HttpSession session = servletRequest.getSession();
        // 세션 키 값
        session.setAttribute(SessionKey.SESSION_KEY, response.getUserId());
        // 세션 만료 시간(30분) 설정
        session.setMaxInactiveInterval(30 * 60);

        return ResponseEntity.ok(response);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest servletRequest) {
        // 로그인하지 않으면 HttpSession -> Null로 반환
        HttpSession session = servletRequest.getSession(false);
        if (session == null) {
            throw new UnauthorizedException("로그인해 주세요.");
        }
        //  세션 존재 시 = 로그인이 된 경우
        // 해당 세션(데이터) 삭제
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
        //  세션 존재 시 = 로그인이 된 경우
        if (session == null) {
            throw new UnauthorizedException("로그인해 주세요.");
        }
        Long sessionUserId = (Long) session.getAttribute(SessionKey.SESSION_KEY);
        if (sessionUserId == null) {
            throw new UnauthorizedException("로그인해 주세요.");
        }
        userService.withdraw(sessionUserId, withdrawRequest);    // DB 삭제
        session.invalidate();    // 해당 세션(데이터) 삭제
        return ResponseEntity.noContent().build();
    }

    // 조회
    @GetMapping("/me")
    public ResponseEntity<UserResponse> findMe(
            HttpServletRequest servletRequest
    ) {
        // 세션 Null 체크
        HttpSession session = servletRequest.getSession(false);
        if (session == null) {
            throw new UnauthorizedException("로그인해 주세요.");
        }
        // 세션에서 사용자 정보 가져오기
        Long sessionUserId = (Long) session.getAttribute(SessionKey.SESSION_KEY);
        if (sessionUserId == null) {
            throw new UnauthorizedException("로그인해 주세요.");
        }
        UserResponse response = userService.findMe(sessionUserId);
        // 세션 갱신
        session.setAttribute(SessionKey.SESSION_KEY, sessionUserId);
        // 30분 연장
        session.setMaxInactiveInterval(30 * 60);
        return ResponseEntity.ok(response);
    }

    // 수정
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updatedMe(
            HttpServletRequest servletRequest,
            @RequestBody UserUpdateRequest updateRequest
    ) {
        // 세션 Null 체크
        HttpSession session = servletRequest.getSession(false);
        if (session == null) {
            throw new UnauthorizedException("로그인해 주세요.");
        }
        // 세션에서 사용자 정보 가져오기
        Long sessionUserId = (Long) session.getAttribute(SessionKey.SESSION_KEY);
        if (sessionUserId == null) {
            throw new UnauthorizedException("로그인해 주세요.");
        }
        // 서비스 호출
        UserResponse updatedUser = userService.updateMe(sessionUserId, updateRequest);
        // 세션 갱신
        session.setAttribute(SessionKey.SESSION_KEY, sessionUserId);
        // 30분 연장
        session.setMaxInactiveInterval(30 * 60);
        return ResponseEntity.ok(updatedUser);
    }
}
