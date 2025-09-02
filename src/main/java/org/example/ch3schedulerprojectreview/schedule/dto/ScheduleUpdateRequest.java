package org.example.ch3schedulerprojectreview.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * JSON 직렬화/역직렬화 시 날짜 포맷을 지정할 때 사용
 * 직렬화: 객체 -> JSON 문자열로 변환
 * 역직렬화: JSON 문자열로 변환 -> 객체로 변환
 */

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
/** @Getter
 * 클래스 내 모든 필드에 getter 메서드 자동 생성
 *
 * cf) Setter 안 쓰는 이유
 * 엔티티의 필드를 어디서든 수정 가능
 * -> 데이터 무결성 위반 & JPA 불변성 위반
 * -> 값은 엔티티를 생성하는 시점 또는 비즈니스 로직 메서드를 통해서만 변경 가능
 *    (생성자에서만 초기화)
 */
public class ScheduleUpdateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    /**
     * 필드가 null 또는 빈 문자열("") 혹은 공백(" ")이면 검증 실패
     * 제목 입력 필수 검증
     */
    @Size(max = 100, message = "제목은 최대 100자 입력 가능합니다.")    // 문자열 길이 검증
    private String title;

    @Size(max = 200, message = "내용은 최대 200자 입력 가능합니다.")    // 문자열 길이 검증
    private String content;

    // Java 8 이상, 날짜
    // 일정 시작 날짜 및 시간
    @NotNull(message = "시작 날짜 및 시간은 필수입니다.")// 예외 시 HTTP 코드 400 Bad Request 반환
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    /**
     * JSON 직렬화/역직렬화 시 "yyyy-MM-dd HH:mm" 포맷 사용
     */
    private LocalDateTime startDateTime;

    // 일정 종료 날짜 및 시간
    @NotNull(message = "종료 날짜 및 시간은 필수입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDateTime;

    // 하루종일 일정 여부, true면 날짜 하루
    // private boolean allDay;
}
