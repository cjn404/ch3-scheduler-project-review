package org.example.ch3schedulerprojectreview.comment.dto;

import lombok.Getter;
import org.example.ch3schedulerprojectreview.schedule.dto.ScheduleResponse;

import java.time.LocalDateTime;

//@Getter
public class CommentResponse {

    private ScheduleResponse schedule;
    private Long id;
    private String userName;
    private String comment;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public CommentResponse(
            ScheduleResponse schedule,
            Long id,
            String userName,
            String comment,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt
    ) {
        this.schedule = schedule;
        this.id = id;
        this.userName = userName;
        this.comment = comment;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public ScheduleResponse getSchedule() {
        return schedule;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }
}
