package org.example.ch3schedulerprojectreview.schedule.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ch3schedulerprojectreview.schedule.dto.ScheduleRequest;
import org.example.ch3schedulerprojectreview.schedule.dto.ScheduleResponse;
import org.example.ch3schedulerprojectreview.schedule.entity.Schedule;
import org.example.ch3schedulerprojectreview.schedule.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    // 생성
    @Transactional    // jakarta는 readOnly 기능 없음
    public ScheduleResponse save(ScheduleRequest request) {
        Schedule schedule = new Schedule(
                request.getTitle(),
                request.getContent(),
                request.getStartDateTime(),
                request.getEndDateTime()
        );
        Schedule savedSchedule = scheduleRepository.save(schedule);

        return new ScheduleResponse(
                savedSchedule.getScheduleId(),
                savedSchedule.getTitle(),
                savedSchedule.getContent(),
                savedSchedule.getStartDateTime(),
                savedSchedule.getEndDateTime()
        );
    }

    // 전체 조회
    @Transactional(readOnly = true)
    public List<ScheduleResponse> findAll() {
        List<Schedule> schedules = scheduleRepository.findAll();
        List<ScheduleResponse> dtos = new ArrayList<>();
        for (Schedule schedule : schedules) {
            ScheduleResponse scheduleResponse = new ScheduleResponse(
                    schedule.getScheduleId(),
                    schedule.getTitle(),
                    schedule.getContent(),
                    schedule.getStartDateTime(),
                    schedule.getEndDateTime()
            );
            dtos.add(scheduleResponse);
        }
        return dtos;
    }

    // 단건 조회
    @Transactional(readOnly = true)
    public ScheduleResponse findById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new EntityNotFoundException("Schedule with id " + scheduleId + " not found")
        );
        return new ScheduleResponse(
                schedule.getScheduleId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getStartDateTime(),
                schedule.getEndDateTime()
        );
    }

    // 수정
    @Transactional
    public ScheduleResponse updateById(Long scheduleId, ScheduleRequest request) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new EntityNotFoundException("Schedule with id " + scheduleId + " not found")
        );
        schedule.updateSchedule(
                request.getTitle(),
                request.getContent(),
                request.getStartDateTime(),
                request.getEndDateTime()
        );
        return new ScheduleResponse(
                schedule.getScheduleId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getStartDateTime(),
                schedule.getEndDateTime()
        );
    }

    // 삭제
    @Transactional
    public void deleteById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new EntityNotFoundException("Schedule with id " + scheduleId + " not found")
        );
        scheduleRepository.delete(schedule);
    }
}
