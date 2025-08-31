package org.example.ch3schedulerprojectreview.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ScheduleDeleteRequest {

    // 비밀번호: 8~16자, 대소문자, 숫자, 특수문자 최소 1개씩 포함
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$")
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
