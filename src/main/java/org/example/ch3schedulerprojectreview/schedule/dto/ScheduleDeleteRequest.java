package org.example.ch3schedulerprojectreview.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

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
public class ScheduleDeleteRequest {

    // 비밀번호: 8~16자, 대소문자, 숫자, 특수문자 최소 1개씩 포함
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$")
    /**
     * 정규표현식으로 비밀번호 형식 검증
     * 의미:
     * (?=.*[a-z])              : 최소 하나 이상의 소문자
     * (?=.*[A-Z])              : 최소 하나 이상의 대문자
     * (?=.*\\d)                : 최소 하나 이상의 숫자
     * (?=.*[@$!%*?&])          : 최소 하나 이상의 특수문자(@ $ ! % * ? &)
     * [A-Za-z\\d@$!%*?&]{8,16} : 전체 길이 8~16자, 허용되는 문자 집합
     */
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    /**
     * 필드가 null 또는 빈 문자열("") 혹은 공백(" ")이면 검증 실패
     * 비밀번호 입력 필수 검증
     */
    private String password;

    /** getter -> 어노테이션으로 자동 생성
     public String getPassword() {
         return password;
     }
     */
}
