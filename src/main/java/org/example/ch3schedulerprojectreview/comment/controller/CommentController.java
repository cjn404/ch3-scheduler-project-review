package org.example.ch3schedulerprojectreview.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ch3schedulerprojectreview.comment.dto.CommentDeleteRequest;
import org.example.ch3schedulerprojectreview.comment.dto.CommentRequest;
import org.example.ch3schedulerprojectreview.comment.dto.CommentResponse;
import org.example.ch3schedulerprojectreview.comment.service.CommentService;
import org.example.ch3schedulerprojectreview.common.constants.auth.SessionKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 생성
    @PostMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<CommentResponse> save(
            @PathVariable Long scheduleId,
            @Valid @RequestBody CommentRequest request,
            @SessionAttribute(name = SessionKey.SESSION_KEY) Long sessionUserId
    ) {
        CommentResponse response = commentService.save(sessionUserId, scheduleId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 유저 아이디 기준 조회
    @GetMapping("/users/{userId}/comments")
    public ResponseEntity<Page<CommentResponse>> findAllByUserId(
            @PathVariable Long userId,
            @PageableDefault Pageable pageable
    ) {
        Page<CommentResponse> response = commentService.findAllByUserId(userId, pageable);
        return ResponseEntity.ok(response);
    }

    // 스케줄 아이디 기준 조회
    @GetMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<Page<CommentResponse>> findAllByScheduleId(
            @PathVariable Long scheduleId,
            @PageableDefault Pageable pageable
    ) {
        Page<CommentResponse> response = commentService.findAllByScheduleId(scheduleId, pageable);
        return ResponseEntity.ok(response);
    }

    // 수정
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> update(
            @PathVariable Long commentId,
            @SessionAttribute(name = SessionKey.SESSION_KEY) Long sessionUserId,
            @Valid @RequestBody CommentRequest request
    ) {
        CommentResponse response = commentService.updateById(commentId, sessionUserId, request);
        return ResponseEntity.ok(response);
    }

    // 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> delete(
            @PathVariable Long commentId,
            @SessionAttribute(name = SessionKey.SESSION_KEY) Long sessionUserId,
            @Valid @RequestBody CommentDeleteRequest request
    ) {
        commentService.deleteById(commentId, sessionUserId, request);
        return ResponseEntity.noContent().build();
    }
}
