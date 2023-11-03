package com.project.socket.comment.controller;

import com.project.socket.comment.controller.dto.request.ModifyCommentRequestDto;
import com.project.socket.comment.service.usecase.ModifyCommentUseCase;
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
public class ModifyCommentController {

  private final ModifyCommentUseCase modifyCommentUseCase;

  @PatchMapping("posts/{postId}/comments/{commentId}")
  public ResponseEntity<Object> modifyComment(
      @PathVariable @Min(1) Long postId,
      @PathVariable @Min(1) Long commentId,
      @RequestBody @Valid ModifyCommentRequestDto modifyCommentRequestDto,
      @AuthenticationPrincipal UserDetails userDetails
  ) {
    long userId = Long.parseLong(userDetails.getUsername());
    modifyCommentUseCase.apply(modifyCommentRequestDto.toCommand(userId, postId, commentId));

    return ResponseEntity.ok().build();
  }
}
