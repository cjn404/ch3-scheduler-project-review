package org.example.ch3schedulerprojectreview.schedule.service;

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
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
/**
 * 비즈니스 로직(Business Logic)을 처리하는 서비스 클래스임을 나타냄
 * 스프링이 Bean으로 자동 등록
 */
@RequiredArgsConstructor
/**
 * final 필드에 대한 생성자 자동 생성
 * 코드 없이도 생성자를 만들어서 userRepository, passwordEncoder 자동 DI(Dependency Injection) 가능
 */
public class ScheduleService {

    // 의존성 주입: Repository와 비밀번호 암호화 도구를 사용
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /** @RequiredArgsConstructor이 없으면,
     * public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
     *     this.userRepository = userRepository;
     *     this.passwordEncoder = passwordEncoder;
     *     }
     */

    /*
    Hibernate:
    select
    u1_0.user_id,
    u1_0.created_at,
    u1_0.deleted,
    u1_0.email,
    u1_0.modified_at,
    u1_0.password,
    u1_0.username
            from
    user u1_0
    where
    u1_0.user_id=?
    Hibernate:
    insert
            into
    schedule
            (content, created_at, deleted, end_date_time, modified_at, start_date_time, title, user_id)
    values
            (?, ?, ?, ?, ?, ?, ?, ?)
    */

    // 생성
    @Transactional    // jakarta는 readOnly 기능 없음
    /**
     * 메서드 내 DB 작업을 트랜잭션 단위로 묶음
     * 정상 종료 -> 커밋, 예외 발생 -> 롤백
     */
    public ScheduleResponse save(Long userId, ScheduleRequest request) {
        User user = userRepository.findById(userId).orElseThrow(    // Optional에서 안전하게 예외를 던지는 표준 방식
                () -> new NotFoundException("해당하는 계정이 없습니다.")
        );
        Schedule schedule = new Schedule(    // Schedule 엔티티 생성
                request.getTitle(),
                request.getContent(),
                request.getStartDateTime(),
                request.getEndDateTime(),
                user
        );
        Schedule savedSchedule = scheduleRepository.save(schedule);    // JPA가 persist 또는 merge를 수행

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

    /*
    Hibernate:
    select
    s1_0.schedule_id,
    s1_0.content,
    s1_0.created_at,
    s1_0.deleted,
    s1_0.end_date_time,
    s1_0.modified_at,
    s1_0.start_date_time,
    s1_0.title,
    s1_0.user_id
            from
    schedule s1_0
    left join
    user u1_0
    on u1_0.user_id=s1_0.user_id
            where
    u1_0.user_id=?
    and not(s1_0.deleted)
    order by
    s1_0.created_at desc
    limit
        ?
    Hibernate:
    select
    count(s1_0.schedule_id)
    from
    schedule s1_0
    left join
    user u1_0
    on u1_0.user_id=s1_0.user_id
            where
    u1_0.user_id=?
    and not(s1_0.deleted)
    Hibernate:
    select
    u1_0.user_id,
    u1_0.created_at,
    u1_0.deleted,
    u1_0.email,
    u1_0.modified_at,
    u1_0.password,
    u1_0.username
            from
    user u1_0
    where
    u1_0.user_id=?
    */

    // 전체 조회
    @Transactional(readOnly = true)
    public Page<ScheduleResponse> findAllMe(Long userId, Pageable pageable) {
        Page<Schedule> schedules = scheduleRepository.findByUserUserIdAndDeletedFalse(userId, pageable);
        // map을 이용해 DTO로 변환
        return schedules.map(schedule -> {
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
        });
    }

    /*
    Hibernate:
    select
    s1_0.schedule_id,
    s1_0.content,
    s1_0.created_at,
    s1_0.deleted,
    s1_0.end_date_time,
    s1_0.modified_at,
    s1_0.start_date_time,
    s1_0.title,
    s1_0.user_id
            from
    schedule s1_0
    where
    s1_0.schedule_id=?
    and not(s1_0.deleted)
    Hibernate:
    select
    u1_0.user_id,
    u1_0.created_at,
    u1_0.deleted,
    u1_0.email,
    u1_0.modified_at,
    u1_0.password,
    u1_0.username
            from
    user u1_0
    where
    u1_0.user_id=?
    */

    // 단건 조회
    @Transactional(readOnly = true)
    public ScheduleResponse findMe(Long scheduleId, Long sessionUserId) {
        Schedule schedule = scheduleRepository.findByScheduleIdAndDeletedFalse(scheduleId).orElseThrow(
                () -> new NotFoundException("해당하는 일정이 없습니다.")
        );

        // NSF
        if (!Objects.equals(schedule.getUser().getUserId(), sessionUserId)) {
            throw new UnauthorizedException("본인 일정만 조회 가능합니다.");
        }

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

    /*
    Hibernate:
    select
    s1_0.schedule_id,
    s1_0.content,
    s1_0.created_at,
    s1_0.deleted,
    s1_0.end_date_time,
    s1_0.modified_at,
    s1_0.start_date_time,
    s1_0.title,
    s1_0.user_id
            from
    schedule s1_0
    where
    s1_0.schedule_id=?
    Hibernate:
    select
    u1_0.user_id,
    u1_0.created_at,
    u1_0.deleted,
    u1_0.email,
    u1_0.modified_at,
    u1_0.password,
    u1_0.username
            from
    user u1_0
    where
    u1_0.user_id=?
    Hibernate:
    update
            schedule
    set
    content=?,
    deleted=?,
    end_date_time=?,
    modified_at=?,
    start_date_time=?,
    title=?,
    user_id=?
    where
    schedule_id=?
    */

    // 수정
    @Transactional
    public ScheduleResponse updateMe(Long scheduleId, Long sessionUserId, ScheduleUpdateRequest updateRequest) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new NotFoundException("해당하는 일정이 없습니다.")
        );
        // NSF
        if (!Objects.equals(schedule.getUser().getUserId(), sessionUserId)) {
            throw new UnauthorizedException("본인 일정만 수정 가능합니다.");
        }

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

    /*
    Hibernate:
    select
    u1_0.user_id,
    u1_0.created_at,
    u1_0.deleted,
    u1_0.email,
    u1_0.modified_at,
    u1_0.password,
    u1_0.username
            from
    user u1_0
    where
    u1_0.user_id=?
    Hibernate:
    update
            schedule
    set
    content=?,
    deleted=?,
    end_date_time=?,
    modified_at=?,
    start_date_time=?,
    title=?,
    user_id=?
    where
    schedule_id=?
    */

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
        // NSF
        if (!Objects.equals(schedule.getUser().getUserId(), sessionUserId)) {
            throw new UnauthorizedException("본인 일정만 삭제 가능합니다.");
        }
        // Soft Delete
        schedule.softDelete();      // deleted = true
//        scheduleRepository.save(schedule);
    }

    /*
    Hibernate:
    select
    s1_0.schedule_id,
    s1_0.content,
    s1_0.created_at,
    s1_0.deleted,
    s1_0.end_date_time,
    s1_0.modified_at,
    s1_0.start_date_time,
    s1_0.title,
    s1_0.user_id
            from
    schedule s1_0
    where
    s1_0.schedule_id=?
    Hibernate:
    select
    u1_0.user_id,
    u1_0.created_at,
    u1_0.deleted,
    u1_0.email,
    u1_0.modified_at,
    u1_0.password,
    u1_0.username
            from
    user u1_0
    where
    u1_0.user_id=?
    Hibernate:
    update
            schedule
    set
    content=?,
    deleted=?,
    end_date_time=?,
    modified_at=?,
    start_date_time=?,
    title=?,
    user_id=?
    where
    schedule_id=?
    */

    // 복구
    @Transactional
    public void restoreById(Long scheduleId, Long sessionUserId, ScheduleDeleteRequest deleteRequest) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new NotFoundException("해당하는 일정이 없습니다.")
        );
        User user = schedule.getUser();
        // NSF
        if (!Objects.equals(user.getUserId(), sessionUserId)) {
            throw new UnauthorizedException("본인 일정만 복구 가능합니다.");
        }
        if (!passwordEncoder.matches(deleteRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");
        }
        // 복구
        schedule.restore();
    }
}
