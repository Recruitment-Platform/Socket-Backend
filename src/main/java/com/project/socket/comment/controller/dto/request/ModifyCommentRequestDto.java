package com.project.socket.comment.controller.dto.request;

import com.project.socket.comment.service.usecase.ModifyCommentCommand;
import jakarta.validation.constraints.NotBlank;

public record ModifyCommentRequestDto(
    @NotBlank String content
) {

  public ModifyCommentCommand toCommand(Long userId, Long postId, Long commentId) {
    return new ModifyCommentCommand(userId, postId, commentId, content);
  }
}
