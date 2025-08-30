package org.example.ch3schedulerprojectreview.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.ch3schedulerprojectreview.user.dto.UserRequest;
import org.example.ch3schedulerprojectreview.user.dto.UserResponse;
import org.example.ch3schedulerprojectreview.user.dto.UserUpdateRequest;
import org.example.ch3schedulerprojectreview.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/me")
public class UserController {

    private final UserService userService;

    // 생성
    @PostMapping
    public ResponseEntity<UserResponse> save(
            @RequestBody UserRequest userRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userRequest));
    }

    // 조회
    @GetMapping("/{userId:\\d+}")
    public ResponseEntity<UserResponse> findById(
            @PathVariable Long userId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(userId));
    }

    // 수정
    @PutMapping("/{userId:\\d+}")
    public ResponseEntity<UserResponse> updateById(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequest updateRequest
    ) {
        return ResponseEntity.ok(userService.updateById(userId, updateRequest));
    }

    // 삭제
    @DeleteMapping("/{userId:\\d+}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long userId
    ) {
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}
