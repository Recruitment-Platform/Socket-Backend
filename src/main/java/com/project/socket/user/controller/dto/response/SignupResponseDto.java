package com.project.socket.user.controller.dto.response;

import com.project.socket.user.model.User;

public record SignupResponseDto(String nickname, String githubLink) {

  public static SignupResponseDto from(User user) {
    return new SignupResponseDto(user.getNickname(), user.getGithubLink());
  }
}
