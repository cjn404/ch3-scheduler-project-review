package org.example.ch3schedulerprojectreview.user.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class UserResponse {

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
    }
}
