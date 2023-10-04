package com.project.socket.user.controller;

import com.project.socket.user.controller.dto.request.SignupRequestDto;
import com.project.socket.user.controller.dto.response.SignupResponseDto;
import com.project.socket.user.model.User;
import com.project.socket.user.service.usecase.SignupUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignupController {

  private final SignupUseCase signupUseCase;

  @PatchMapping("/signup")
  public ResponseEntity<SignupResponseDto> signup(@RequestBody @Valid SignupRequestDto signupRequestDto,
      @AuthenticationPrincipal UserDetails userDetails) {
    long userId = Long.parseLong(userDetails.getUsername());
    User updatedUser = signupUseCase.signup(signupRequestDto.toCommand(userId));
    return ResponseEntity.ok(SignupResponseDto.from(updatedUser));
  }
}
