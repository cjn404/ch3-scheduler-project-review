package org.example.ch3schedulerprojectreview.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class UserLoginRequest {

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    /**
     * 해당 필드가 올바른 이메일 형식인지 검증
     * 실패 시 지정한 메시지를 에러 메시지로 반환
     */
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    /**
     * 필드가 null 또는 빈 문자열("") 혹은 공백(" ")이면 검증 실패
     * 이메일 입력 필수임을 보장
     */
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    /**
     * 비밀번호 입력 필수 검증
     */
    public String password;

    /** getter -> 어노테이션으로 자동 생성
    private String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    */
}
