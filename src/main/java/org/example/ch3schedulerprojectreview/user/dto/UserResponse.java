package org.example.ch3schedulerprojectreview.user.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponse {

    // final 키워드 -> 한 번 초기화되면 변경 불가(불변 객체)
    private final Long userId;
    private final String email;
    private final String userName;
    private final LocalDateTime createAt;
    private final LocalDateTime modifiedAt;

    public UserResponse(
            Long userId,
            String email,
            String userName,
            LocalDateTime createAt,
            LocalDateTime modifiedAt) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;

      /** 생성자(Constructor)
       * DTO 객체 생성 시 모든 필드를 한 번에 초기화
       * final 필드이므로 반드시 생성자에서 초기화 필요
       * 생성 후에는 값 변경 불가
      */
    }
}
