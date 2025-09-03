package org.example.ch3schedulerprojectreview.comment.service;

import lombok.RequiredArgsConstructor;
import org.example.ch3schedulerprojectreview.comment.dto.CommentDeleteRequest;
import org.example.ch3schedulerprojectreview.comment.dto.CommentRequest;
import org.example.ch3schedulerprojectreview.comment.dto.CommentResponse;
import org.example.ch3schedulerprojectreview.comment.entity.Comment;
import org.example.ch3schedulerprojectreview.comment.repository.CommentRepository;
import org.example.ch3schedulerprojectreview.common.exception.custom.NotFoundException;
import org.example.ch3schedulerprojectreview.common.exception.custom.UnauthorizedException;
import org.example.ch3schedulerprojectreview.config.PasswordEncoder;
import org.example.ch3schedulerprojectreview.schedule.dto.ScheduleResponse;
import org.example.ch3schedulerprojectreview.schedule.entity.Schedule;
import org.example.ch3schedulerprojectreview.schedule.repository.ScheduleRepository;
import org.example.ch3schedulerprojectreview.user.entity.User;
import org.example.ch3schedulerprojectreview.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
//@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CommentService(
            CommentRepository commentRepository,
            ScheduleRepository scheduleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder)
    {
        this.commentRepository = commentRepository;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
    insert
    into
        comment
        (comment, created_at, deleted, modified_at, schedule_id, user_id)
    values
        (?, ?, ?, ?, ?, ?)

    */

    // 생성
    @Transactional
    public CommentResponse save(Long userId, Long scheduleId, CommentRequest request) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new NotFoundException("해당하는 일정이 없습니다.")
        );
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("해당하는 유저가 없습니다.")
        );
        Comment comment = new Comment(user, schedule, request.getComment());
        Comment savedComment = commentRepository.save(comment);
        ScheduleResponse scheduleResponse = new ScheduleResponse(
                schedule.getUser().getUserId(),
                schedule.getUser().getEmail(),
                schedule.getUser().getUsername(),
                schedule.getScheduleId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getStartDateTime(),
                schedule.getEndDateTime()
        );
        return new CommentResponse(
                scheduleResponse,
                savedComment.getId(),
                savedComment.getUser().getUsername(),
                savedComment.getComment(),
                savedComment.getCreatedAt(),
                savedComment.getModifiedAt()
        );
    }

    /*
    Hibernate:
    select
        c1_0.id,
        c1_0.comment,
        c1_0.created_at,
        c1_0.deleted,
        c1_0.modified_at,
        s1_0.schedule_id,
        s1_0.content,
        s1_0.created_at,
        s1_0.deleted,
        s1_0.end_date_time,
        s1_0.modified_at,
        s1_0.start_date_time,
        s1_0.title,
        s1_0.user_id,
        u1_0.user_id,
        u1_0.created_at,
        u1_0.deleted,
        u1_0.email,
        u1_0.modified_at,
        u1_0.password,
        u1_0.username
    from
        comment c1_0
    join
        user u1_0
            on u1_0.user_id=c1_0.user_id
    join
        schedule s1_0
            on s1_0.schedule_id=c1_0.schedule_id
    where
        u1_0.user_id=?
    order by
        c1_0.created_at desc
    limit
        ?
    */

    // 유저 아이디 기준 전체 조회
    @Transactional(readOnly = true)
    public Page<CommentResponse> findAllByUserId(Long userId, Pageable pageable) {
        // 유저 기준 댓글 전체 조회
        Page<Comment> comments = commentRepository.findAllByUser_UserId(userId, pageable);    // 댓글 목록 조회
        return comments.map(comment -> {
            Schedule schedule = comment.getSchedule();    // 댓글 달린 일정 조회. SELECT * FROM schedule WHERE schedule_id = ?
            ScheduleResponse scheduleResponse = new ScheduleResponse(
                    schedule.getUser().getUserId(),    // 일정마다 유저를 DB에서 가져오기 위해 쿼리 발생
                    schedule.getUser().getEmail(),
                    schedule.getUser().getUsername(),
                    schedule.getScheduleId(),
                    schedule.getTitle(),
                    schedule.getContent(),
                    schedule.getStartDateTime(),
                    schedule.getEndDateTime()
            );
            return new CommentResponse(
                    scheduleResponse,
                    comment.getId(),
                    comment.getUser().getUsername(),    // 댓글마다 유저를 DB에서 가져오기 위해 쿼리 발생
                    comment.getComment(),
                    comment.getCreatedAt(),
                    comment.getModifiedAt());
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
Hibernate:
    select
        c1_0.id,
        c1_0.comment,
        c1_0.created_at,
        c1_0.deleted,
        c1_0.modified_at,
        s1_0.schedule_id,
        s1_0.content,
        s1_0.created_at,
        s1_0.deleted,
        s1_0.end_date_time,
        s1_0.modified_at,
        s1_0.start_date_time,
        s1_0.title,
        s1_0.user_id,
        u1_0.user_id,
        u1_0.created_at,
        u1_0.deleted,
        u1_0.email,
        u1_0.modified_at,
        u1_0.password,
        u1_0.username
    from
        comment c1_0
    join
        user u1_0
            on u1_0.user_id=c1_0.user_id
    join
        schedule s1_0
            on s1_0.schedule_id=c1_0.schedule_id
    where
        u1_0.user_id=?
    order by
        c1_0.created_at desc
    limit
        ?
    */

    // 일정 아이디 기준 전체 조회
    @Transactional(readOnly = true)
    public Page<CommentResponse> findAllByScheduleId(Long scheduleId, Pageable pageable) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new NotFoundException("해당하는 댓글이 없습니다.")
        );
        Page<Comment> comments = commentRepository.findAllBySchedule_ScheduleId(scheduleId, pageable);
        ScheduleResponse scheduleResponse = new ScheduleResponse(
                schedule.getUser().getUserId(),
                schedule.getUser().getEmail(),
                schedule.getUser().getUsername(),
                schedule.getScheduleId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getStartDateTime(),
                schedule.getEndDateTime()
        );
        return comments.map(comment ->
            new CommentResponse(
                    scheduleResponse,
                    comment.getId(),
                    comment.getUser().getUsername(),
                    comment.getComment(),
                    comment.getCreatedAt(),
                    comment.getModifiedAt()));
    }

    /*
    Hibernate:
    select
        c1_0.id,
        c1_0.comment,
        c1_0.created_at,
        c1_0.deleted,
        c1_0.modified_at,
        c1_0.schedule_id,
        c1_0.user_id
    from
        comment c1_0
    where
        c1_0.id=?
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
        comment
    set
        comment=?,
        deleted=?,
        modified_at=?,
        schedule_id=?,
        user_id=?
    where
        id=?
    */

    // 수정
    @Transactional
    public CommentResponse updateById(Long commentId, Long sessionUserId, CommentRequest request) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("해당하는 댓글이 없습니다.")
        );
        if (!Objects.equals(comment.getUser().getUserId(), sessionUserId)) {
            throw new UnauthorizedException("본인 댓글만 수정 가능합니다.");
        }
        comment.updateComment(request.getComment());

        Schedule schedule = comment.getSchedule();
        ScheduleResponse scheduleResponse = new ScheduleResponse(
                schedule.getUser().getUserId(),
                schedule.getUser().getEmail(),
                schedule.getUser().getUsername(),
                schedule.getScheduleId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getStartDateTime(),
                schedule.getEndDateTime()
        );
        return new CommentResponse(
                scheduleResponse,
                comment.getId(),
                comment.getUser().getUsername(),
                comment.getComment(),
                comment.getCreatedAt(),
                comment.getModifiedAt());
    }

    /*
    Hibernate:
    select
        c1_0.id,
        c1_0.comment,
        c1_0.created_at,
        c1_0.deleted,
        c1_0.modified_at,
        c1_0.schedule_id,
        c1_0.user_id
    from
        comment c1_0
    where
        c1_0.id=?
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
        comment
    set
        comment=?,
        deleted=?,
        modified_at=?,
        schedule_id=?,
        user_id=?
    where
        id=?
    */

    // 삭제
    @Transactional
    public void deleteById(Long commentId, Long sessionUserId, CommentDeleteRequest deleteRequest) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("해당하는 댓글이 없습니다.")
        );
//        User user = comment.getUser();
        if(!passwordEncoder.matches(deleteRequest.getPassword(), comment.getUser().getPassword())) {
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");
        }
        if (!Objects.equals(comment.getUser().getUserId(), sessionUserId)) {
            throw new UnauthorizedException("본인 댓글만 삭제 가능합니다.");
        }
        comment.softDelete();
//        commentRepository.save(comment);    // 명시적과 다르다. 걍 중복 행위 Save는 준영속->영속 상태로 바꾸는 것
    }
}
