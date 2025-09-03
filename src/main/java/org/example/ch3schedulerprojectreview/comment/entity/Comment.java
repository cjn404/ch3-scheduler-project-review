package org.example.ch3schedulerprojectreview.comment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.ch3schedulerprojectreview.common.entity.BaseEntity;
import org.example.ch3schedulerprojectreview.schedule.entity.Schedule;
import org.example.ch3schedulerprojectreview.user.entity.User;

@Entity
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @Column(length = 255, nullable = false)
    private String comment;

    protected Comment() {}

    public Comment(User user, Schedule schedule, String comment) {
        this.user = user;
        this.schedule = schedule;
        this.comment = comment;
    }

    public void updateComment(String comment) {
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public String getComment() {
        return comment;
    }
}
