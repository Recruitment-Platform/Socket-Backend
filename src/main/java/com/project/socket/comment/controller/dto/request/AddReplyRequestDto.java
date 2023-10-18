package com.project.socket.comment.controller.dto.request;

import com.project.socket.comment.service.usecase.AddReplyCommand;
import jakarta.validation.constraints.NotBlank;

public record AddReplyRequestDto(
    @NotBlank String content
) {

  public AddReplyCommand toCommand(Long userId, Long postId, Long parentId) {
    return new AddReplyCommand(content, userId, postId, parentId);
  }
}
