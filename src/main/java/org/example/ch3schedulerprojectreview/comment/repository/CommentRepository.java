package org.example.ch3schedulerprojectreview.comment.repository;

import org.example.ch3schedulerprojectreview.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c " +
            "join fetch c.user " +
            "join fetch c.schedule " +
            "where c.user.userId = :userId")
    Page<Comment> findAllByUser_UserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select c from Comment c " +
            "join fetch c.user " +
            "join fetch c.schedule " +
            "where c.user.userId = :scheduleId")
    Page<Comment> findAllBySchedule_ScheduleId(@Param("scheduleId") Long scheduleId, Pageable pageable);
}
