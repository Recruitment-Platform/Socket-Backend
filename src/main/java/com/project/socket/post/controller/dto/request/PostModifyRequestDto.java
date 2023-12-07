package com.project.socket.post.controller.dto.request;

import com.project.socket.common.ModifyInputValidCheck.StringInputCheck;
import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostType;
import com.project.socket.post.service.usecase.PostModifyCommand;

public record PostModifyRequestDto(@StringInputCheck String title,
                                   @StringInputCheck String postContent,
                                   PostType postType,
                                   PostMeeting postMeeting) {


  public PostModifyCommand toCommand(Long userId, Long postId) {
    return new PostModifyCommand(userId, postId, title, postContent, postType, postMeeting);
  }


}
