package org.example.ch3schedulerprojectreview.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleUpdateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 100, message = "제목은 최대 100자 입력 가능합니다.")
    private String title;

    @Size(max = 200, message = "내용은 최대 200자 입력 가능합니다.")
    private String content;

    // Java 8 이상, 날짜
    // 일정 시작 날짜 및 시간
    @NotNull(message = "시작 날짜 및 시간은 필수입니다.")    // 예외 시 HTTP 코드 400 Bad Request 반환. 기능상 무의미하나, 메세지용
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")    // 예외 시 HTTP 코드 400 Bad Request 반환
    private LocalDateTime startDateTime;

    // 일정 종료 날짜 및 시간
    @NotNull(message = "종료 날짜 및 시간은 필수입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDateTime;

    // 하루종일 일정 여부, true면 날짜 하루
    // private boolean allDay;
}
