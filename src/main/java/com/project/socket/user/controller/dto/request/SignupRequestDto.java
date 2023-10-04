package com.project.socket.user.controller.dto.request;

import com.project.socket.user.service.SignupCommand;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record SignupRequestDto(@NotBlank String nickname, @URL String githubLink) {

  public SignupCommand toCommand(Long userId) {
    return new SignupCommand(userId, nickname, githubLink);
  }
}
