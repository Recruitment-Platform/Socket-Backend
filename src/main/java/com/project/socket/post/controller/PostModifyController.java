package com.project.socket.post.controller;

import com.project.socket.post.controller.dto.request.PostModifyRequestDto;
import com.project.socket.post.service.usecase.PostModifyUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@Valid
public class PostModifyController {

  private final PostModifyUseCase postModifyUseCase;

  @PatchMapping("posts/{postId}")
  public ResponseEntity<Object> modifyPost(
      @PathVariable @Min(1) Long postId,
      @RequestBody @Validated PostModifyRequestDto postModifyRequestDto,
      @AuthenticationPrincipal UserDetails userDetails
  ) {
    long userId = Long.parseLong(userDetails.getUsername());

    postModifyUseCase.modifyPostNew(postModifyRequestDto.toCommand(userId, postId));

    return ResponseEntity.ok().build();
  }


}
