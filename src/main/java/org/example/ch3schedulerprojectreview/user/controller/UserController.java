package org.example.ch3schedulerprojectreview.user.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ch3schedulerprojectreview.common.constants.auth.SessionKey;
import org.example.ch3schedulerprojectreview.user.dto.UserLoginRequest;
import org.example.ch3schedulerprojectreview.user.dto.UserRequest;
import org.example.ch3schedulerprojectreview.user.dto.UserResponse;
import org.example.ch3schedulerprojectreview.user.dto.UserUpdateRequest;
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
        try {
            UserResponse response = userService.signup(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @Valid @RequestBody UserLoginRequest loginRequest,
            HttpServletRequest servletRequest
    ) {
        try {
            UserResponse response = userService.login(loginRequest);

            // 신규 세션 생성
            HttpSession session = servletRequest.getSession();
            // 세션 키 값
            session.setAttribute(SessionKey.SESSION_KEY, response);
            // 세션 만료 시간(30분) 설정
            session.setMaxInactiveInterval(30 * 60);

            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest servletRequest) {
        // 로그인하지 않으면 HttpSession -> Null로 반환
        HttpSession session = servletRequest.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        //  세션 존재 시 = 로그인이 된 경우
        // 해당 세션(데이터) 삭제
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    // 회원탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<Void> withdraw(HttpServletRequest servletRequest) {
        // 로그인하지 않으면 HttpSession -> Null로 반환
        HttpSession session = servletRequest.getSession(false);
        //  세션 존재 시 = 로그인이 된 경우
        if (session != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserResponse response = (UserResponse) session.getAttribute(SessionKey.SESSION_KEY);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            userService.withdraw(response.getUserId());    // DB 삭제
            session.invalidate();    // 해당 세션(데이터) 삭제
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 조회
    @GetMapping("/{userId:\\d+}")
    public ResponseEntity<UserResponse> findById(
            @PathVariable Long userId
    ) {
        try {
            UserResponse response = userService.findById(userId);
            return ResponseEntity.ok(userService.findById(userId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 수정
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updatedUser(
            HttpServletRequest servletRequest,
            @RequestBody UserUpdateRequest updateRequest
    ) {
        // 세션 Null 체크
        HttpSession session = servletRequest.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // 세션에서 사용자 정보 가져오기
        UserResponse response = (UserResponse) session.getAttribute(SessionKey.SESSION_KEY);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            // 서비스 호출
            UserResponse updatedUser = userService.updateById(response.getUserId(), updateRequest);
            // 세션 갱신
            session.setAttribute(SessionKey.SESSION_KEY, updatedUser);
            // 30분 연장
            session.setMaxInactiveInterval(30 * 60);
            return ResponseEntity.ok(updatedUser);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
