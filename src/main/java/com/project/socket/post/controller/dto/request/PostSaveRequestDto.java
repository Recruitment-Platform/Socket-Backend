package com.project.socket.post.controller.dto.request;

import com.project.socket.common.EnumValidCheck.EnumCheck;
import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostType;
import com.project.socket.post.service.usecase.PostSaveCommand;
import jakarta.validation.constraints.NotBlank;

public record PostSaveRequestDto(@NotBlank String title, @NotBlank String postContent,
                                 @EnumCheck(enumClass = PostType.class, message = "유효 하지 않은 포스트타입 입니다.") PostType postType,
                                 @EnumCheck(enumClass = PostMeeting.class, message = "유효 하지 않은 포스트미팅 입니다.") PostMeeting postMeeting) {

  public PostSaveCommand toCommand(Long userId) {
    return new PostSaveCommand(title, postContent, postType, postMeeting, userId);
  }


}
