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
import org.example.ch3schedulerprojectreview.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

//    public ScheduleController() {
//
//    }
//
//    @Autowired    // * 옵션이 아니라 정석(필수)
//    public ScheduleController(ScheduleService scheduleService) {
//        this.scheduleService = scheduleService;
//    }

    // 생성
    @PostMapping
    public ResponseEntity<ScheduleResponse> save(
            @Valid @RequestBody ScheduleRequest request,
            HttpServletRequest httpServletRequest    // @SessionAttribute
    ) {
        HttpSession session = httpServletRequest.getSession(false);
        Long sessionUserId = (Long) session.getAttribute(SessionKey.SESSION_KEY);

        ScheduleResponse response = scheduleService.save(sessionUserId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 전체 조회
    @GetMapping
    public ResponseEntity<Page<ScheduleResponse>> findAllMe(    // * 메서드명이 의미가 불명확하다 -> findAll
            HttpServletRequest httpServletRequest,
            @RequestParam(defaultValue = "1") int page,    // * Pageable pageable -> 인터페이스이고, 해당 구현체를 스프링에서 자동 객체 생성. 페이징 처리에 필요한 정보들을 넣어준다
            @RequestParam(defaultValue = "10") int size    // * @PageableDefault(size = 10) Pageable pageable

    ) {
        HttpSession session = httpServletRequest.getSession(false);
        Long sessionUserId = (Long) session.getAttribute(SessionKey.SESSION_KEY);

        // 정렬 고정(생성일 기준, 내림차순)
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());    // * sort값을 쿼리스트링에 넣으면
        Page<ScheduleResponse> responses = scheduleService.findAllMe(sessionUserId, pageable);
        return ResponseEntity.ok(responses);
    }

    // 단건 조회
    @GetMapping("/{scheduleId:\\d+}")    // 숫자가 1개 이상 연속된 문자열만 허용. 그 외 입력 시 404 Not Found 반환 * -> 양날의 검: 정확한 이유가 있다. API를 잘 설계하면 무조건적으로 필요하진 않다. 필요한 상황이 따로 있다. 슬러그...? 방어용...? 낫 파운드가 아니라 클라이언트가 잘못 입력한 거 아니냐 예외 터지는 위치가 콘트롤러보다 앞에서 터지는데 무슨 문제인지 추적이 안 된다(로깅도 안 되어있으면...)
    public ResponseEntity<ScheduleResponse> findMe(     // * 메서드명이 의미가 불명확하다 -> findById
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
    @PostMapping("/{scheduleId:\\d+}/restore")     // 복구보다는 체인지스테이터스-> 사용성 측면에서 게시글 자체가 "단건"으로 상태 변화할 일이 있는가...계정 하나를 복구하면 어차피 될 거 아닌가
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
