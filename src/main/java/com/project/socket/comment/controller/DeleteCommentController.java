package com.project.socket.comment.controller;

import com.project.socket.comment.service.usecase.DeleteCommentCommand;
import com.project.socket.comment.service.usecase.DeleteCommentUseCase;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class DeleteCommentController {

  private final DeleteCommentUseCase deleteCommentUseCase;

  @DeleteMapping("posts/{postId}/comments/{commentId}")
  public ResponseEntity<Object> deleteComment(
      @PathVariable @Min(1) Long postId,
      @PathVariable @Min(1) Long commentId,
      @AuthenticationPrincipal UserDetails userDetails
  ){
    Long userId = Long.valueOf(userDetails.getUsername());
    deleteCommentUseCase.accept(new DeleteCommentCommand(userId, postId, commentId));

    return ResponseEntity.noContent().build();
  }
}
