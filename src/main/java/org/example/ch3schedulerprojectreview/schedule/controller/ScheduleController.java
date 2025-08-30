package org.example.ch3schedulerprojectreview.schedule.controller;

import lombok.RequiredArgsConstructor;
import org.example.ch3schedulerprojectreview.schedule.dto.ScheduleRequest;
import org.example.ch3schedulerprojectreview.schedule.dto.ScheduleResponse;
import org.example.ch3schedulerprojectreview.schedule.service.ScheduleService;
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
            @RequestBody ScheduleRequest scheduleRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.save(scheduleRequest));
    }

    // 전체 조회
    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> findAll() {
//        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.findAll());
        return ResponseEntity.ok(scheduleService.findAll());
    }

    // 단건 조회
    @GetMapping("/{scheduleId:\\d+}")    // 숫자가 1개 이상 연속된 문자열만 허용. 그 외 입력 시 404 Not Found 반환
    public ResponseEntity<ScheduleResponse> findById(
            @PathVariable Long scheduleId
    ) {
        return ResponseEntity.ok(scheduleService.findById(scheduleId));
    }

    // 수정
    @PutMapping("/{scheduleId:\\d+}")
    public ResponseEntity<ScheduleResponse> updateById(
            @PathVariable Long scheduleId,
            @RequestBody ScheduleRequest scheduleRequest
    ) {
        return ResponseEntity.ok(scheduleService.updateById(scheduleId, scheduleRequest));
    }

    // 삭제
    @DeleteMapping("/{scheduleId:\\d+}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long scheduleId
    ) {
        scheduleService.deleteById(scheduleId);
        return ResponseEntity.noContent().build();
    }
}
