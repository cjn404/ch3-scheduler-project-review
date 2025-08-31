package org.example.ch3schedulerprojectreview.schedule.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
// 직렬화 관점: 응답 바디(JSON)로 내보낼 때(Jackson) getter만 있으면 충분
public class ScheduleResponse {

    // 필드 final = 완전 불변
    private final Long userId;
    private final String email;
    private final String userName;
    private final Long scheduleId;
    private final String title;
    private final String content;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    // 생성자: final 필드에 값 넣어주는 메서드
    public ScheduleResponse(
            Long userId,
            String email,
            String userName,
            Long scheduleId,
            String title,
            String content,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    ) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.scheduleId = scheduleId;    // 좌: 객체의 필드. 우: 생성자의 매개변수
        this.title = title;
        this.content = content;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /** getter -> 어노테이션으로 자동 생성
    public Long getScheduleId() {
        return scheduleId;
    }
    */
}
