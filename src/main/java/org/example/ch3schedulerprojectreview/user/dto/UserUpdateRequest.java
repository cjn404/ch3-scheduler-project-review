package org.example.ch3schedulerprojectreview.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserUpdateRequest {

    // 비밀번호: 8~16자, 대소문자, 숫자, 특수문자 최소 1개씩 포함
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$")
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;

    @NotBlank(message = "사용자 이름은 필수입니다.")
    @Size(min = 2, max = 30, message = "사용자 이름은 2~30자입니다.")
    private String userName;
}
