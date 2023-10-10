package com.project.socket.comment.controller.dto.request;

import com.project.socket.comment.service.usecase.AddCommentCommand;
import jakarta.validation.constraints.NotBlank;

public record AddCommentRequestDto(@NotBlank String content) {

  public AddCommentCommand toCommand(Long userId, Long postId) {
    return new AddCommentCommand(content, userId, postId);
  }
}
