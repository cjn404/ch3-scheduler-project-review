package org.example.ch3schedulerprojectreview.comment.repository;

import org.example.ch3schedulerprojectreview.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByUser_UserId(Long userId, Pageable pageable);

    Page<Comment> findAllBySchedule_ScheduleId(Long scheduleId, Pageable pageable);
}
