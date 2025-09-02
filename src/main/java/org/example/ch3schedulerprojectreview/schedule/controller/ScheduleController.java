package org.example.ch3schedulerprojectreview.schedule.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ch3schedulerprojectreview.common.constants.auth.SessionKey;
import org.example.ch3schedulerprojectreview.schedule.dto.ScheduleDeleteRequest;
import org.example.ch3schedulerprojectreview.schedule.dto.ScheduleRequest;
import org.example.ch3schedulerprojectreview.schedule.dto.ScheduleResponse;
import org.example.ch3schedulerprojectreview.schedule.dto.ScheduleUpdateRequest;
import org.example.ch3schedulerprojectreview.schedule.service.ScheduleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 생성
    @PostMapping
    public ResponseEntity<ScheduleResponse> save(
            @Valid @RequestBody ScheduleRequest request,
            HttpServletRequest httpServletRequest
    ) {
        HttpSession session = httpServletRequest.getSession(false);
        Long sessionUserId = (Long) session.getAttribute(SessionKey.SESSION_KEY);

        ScheduleResponse response = scheduleService.save(sessionUserId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 전체 조회
    @GetMapping
    public ResponseEntity<Page<ScheduleResponse>> findAllMe(
            HttpServletRequest httpServletRequest,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        HttpSession session = httpServletRequest.getSession(false);
        Long sessionUserId = (Long) session.getAttribute(SessionKey.SESSION_KEY);

        // 정렬 고정(생성일 기준, 내림차순)
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<ScheduleResponse> responses = scheduleService.findAllMe(sessionUserId, pageable);
        return ResponseEntity.ok(responses);
    }

    // 단건 조회
    @GetMapping("/{scheduleId:\\d+}")    // 숫자가 1개 이상 연속된 문자열만 허용. 그 외 입력 시 404 Not Found 반환
    public ResponseEntity<ScheduleResponse> findMe(
            @PathVariable Long scheduleId,
            HttpServletRequest httpServletRequest
    ) {
        HttpSession session = httpServletRequest.getSession(false);
        Long sessionUserId = (Long) session.getAttribute(SessionKey.SESSION_KEY);

        ScheduleResponse response = scheduleService.findMe(scheduleId, sessionUserId);
        return ResponseEntity.ok(response);
    }

    // 수정
    @PutMapping("/{scheduleId:\\d+}")
    public ResponseEntity<ScheduleResponse> updateMe(
            @PathVariable Long scheduleId,
            HttpServletRequest httpServletRequest,
            @Valid @RequestBody ScheduleUpdateRequest updateRequest
    ) {
        HttpSession session = httpServletRequest.getSession(false);
        Long sessionUserId = (Long) session.getAttribute(SessionKey.SESSION_KEY);

        ScheduleResponse response = scheduleService.updateMe(scheduleId, sessionUserId, updateRequest);
        return ResponseEntity.ok(response);
    }

    // 삭제
    @DeleteMapping("/{scheduleId:\\d+}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long scheduleId,
            HttpServletRequest httpServletRequest,
            @Valid @RequestBody ScheduleDeleteRequest deleteRequest
    ) {
        HttpSession session = httpServletRequest.getSession(false);
        Long sessionUserId = (Long) session.getAttribute(SessionKey.SESSION_KEY);

        scheduleService.deleteById(scheduleId, sessionUserId, deleteRequest);
        return ResponseEntity.noContent().build();
    }

    // 복구
    @PostMapping("/{scheduleId:\\d+}/restore")
    public ResponseEntity<ScheduleResponse> restoreById(
            @PathVariable Long scheduleId,
            HttpServletRequest httpServletRequest,
            @RequestBody ScheduleDeleteRequest deleteRequest
    ) {
        HttpSession session = httpServletRequest.getSession(false);
        Long sessionUserId = (Long) session.getAttribute(SessionKey.SESSION_KEY);

        scheduleService.restoreById(scheduleId, sessionUserId, deleteRequest);
        return ResponseEntity.ok().build();
    }
}
