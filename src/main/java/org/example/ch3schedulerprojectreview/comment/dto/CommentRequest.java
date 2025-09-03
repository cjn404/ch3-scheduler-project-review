package org.example.ch3schedulerprojectreview.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentRequest {

    @NotBlank(message = "댓글 내용은 필수 입력값입니다.")
    @Size(min = 1, max = 255, message = "내용은 최대 255자 입력 가능합니다.")
    private String comment;
}
