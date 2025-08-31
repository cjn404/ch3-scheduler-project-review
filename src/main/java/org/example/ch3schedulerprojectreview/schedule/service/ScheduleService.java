package org.example.ch3schedulerprojectreview.schedule.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ch3schedulerprojectreview.common.exception.custom.NotFoundException;
import org.example.ch3schedulerprojectreview.common.exception.custom.UnauthorizedException;
import org.example.ch3schedulerprojectreview.config.PasswordEncoder;
import org.example.ch3schedulerprojectreview.schedule.dto.ScheduleDeleteRequest;
import org.example.ch3schedulerprojectreview.schedule.dto.ScheduleRequest;
import org.example.ch3schedulerprojectreview.schedule.dto.ScheduleResponse;
import org.example.ch3schedulerprojectreview.schedule.dto.ScheduleUpdateRequest;
import org.example.ch3schedulerprojectreview.schedule.entity.Schedule;
import org.example.ch3schedulerprojectreview.schedule.repository.ScheduleRepository;
import org.example.ch3schedulerprojectreview.user.entity.User;
import org.example.ch3schedulerprojectreview.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 생성
    @Transactional    // jakarta는 readOnly 기능 없음
    public ScheduleResponse save(Long userId, ScheduleRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("해당하는 계정이 없습니다.")
        );
        Schedule schedule = new Schedule(
                request.getTitle(),
                request.getContent(),
                request.getStartDateTime(),
                request.getEndDateTime(),
                user
        );
        Schedule savedSchedule = scheduleRepository.save(schedule);

        return new ScheduleResponse(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),
                savedSchedule.getScheduleId(),
                savedSchedule.getTitle(),
                savedSchedule.getContent(),
                savedSchedule.getStartDateTime(),
                savedSchedule.getEndDateTime()
        );
    }

    // 전체 조회
    @Transactional(readOnly = true)
    public List<ScheduleResponse> findAllMe(Long userId) {
        List<Schedule> schedules = scheduleRepository.findByUserUserId(userId);
        List<ScheduleResponse> dtos = new ArrayList<>();
        for (Schedule schedule : schedules) {
            User user = schedule.getUser();

            ScheduleResponse scheduleResponse = new ScheduleResponse(
                    user.getUserId(),
                    user.getEmail(),
                    user.getUsername(),
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
    public ScheduleResponse findMe(Long scheduleId, Long sessionUserId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new NotFoundException("해당하는 일정이 없습니다.")
        );
        User user = schedule.getUser();

        return new ScheduleResponse(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),
                schedule.getScheduleId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getStartDateTime(),
                schedule.getEndDateTime()
        );
    }

    // 수정
    @Transactional
    public ScheduleResponse updateMe(Long scheduleId, Long sessionUserId, ScheduleUpdateRequest updateRequest) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new NotFoundException("해당하는 일정이 없습니다.")
        );
        schedule.updateSchedule(
                updateRequest.getTitle(),
                updateRequest.getContent(),
                updateRequest.getStartDateTime(),
                updateRequest.getEndDateTime()
        );
        User user = schedule.getUser();

        return new ScheduleResponse(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),
                schedule.getScheduleId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getStartDateTime(),
                schedule.getEndDateTime()
        );
    }

    // 삭제
    @Transactional
    public void deleteById(Long scheduleId, Long sessionUserId, ScheduleDeleteRequest deleteRequest) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new NotFoundException("해당하는 일정이 없습니다.")
        );
        User user = schedule.getUser();
        if (!passwordEncoder.matches(deleteRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");
        }
        // Soft Delete
        schedule.softDelete();      // deleted = true
    }

    // 복구
    @Transactional
    public void restoreById(Long scheduleId, Long sessionUserId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new NotFoundException("해당하는 일정이 없습니다.")
        );
        // NSF
        if (!Objects.equals(schedule.getUser().getUserId(), sessionUserId)) {
            throw new UnauthorizedException("본인 일정만 복구 가능합니다.");
        }
        // 복구
        schedule.restore();
    }
}
