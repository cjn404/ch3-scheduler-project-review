package org.example.ch3schedulerprojectreview.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class UserRequest {

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    /**
     * 해당 필드가 올바른 이메일 형식인지 검증
     * 실패 시 지정한 메시지를 에러 메시지로 반환
     */
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    /**
     * 정규표현식으로 이메일 형식 검증용
     * 의미:
     * ^                 : 문자열 시작
     * [a-zA-Z0-9._%+-]+ : @ 앞부분, 알파벳 대소문자, 숫자, 점(.), 언더바(_), %, +, - 중 1개 이상
     * @                 : 반드시 @ 기호 포함
     * [a-zA-Z0-9.-]+    : 도메인 이름, 알파벳 대소문자, 숫자, 점(.), 하이픈(-) 중 1개 이상
     * \\.               : 실제 점(.) 문자. 정규식에서 특수문자 점(.)은 \\로 이스케이프
     * [a-zA-Z]{2,}      : 최상위 도메인(TLD), 최소 2글자 이상, 알파벳만 허용
     * $                 : 문자열 끝
     */
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    /**
     * 필드가 null 또는 빈 문자열("") 혹은 공백(" ")이면 검증 실패
     * 이메일 입력 필수임을 보장
     */
    private String email;

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
     * 비밀번호 입력 필수 검증
     */
    private String password;

    @NotBlank(message = "사용자 이름은 필수입니다.")
    /**
     * 사용자 이름 입력 필수 검증
     */
    @Size(min = 2, max = 30, message = "사용자 이름은 2~30자입니다.")    // 문자열 길이 검증
    private String userName;

    /** getter -> 어노테이션으로 자동 생성
    public String getEmil() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }
    */
}
