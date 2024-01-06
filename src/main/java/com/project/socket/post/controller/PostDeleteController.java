package com.project.socket.post.controller;

import com.project.socket.post.service.usecase.PostDeleteCommand;
import com.project.socket.post.service.usecase.PostDeleteUseCase;
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
public class PostDeleteController {

  private final PostDeleteUseCase postDeleteUseCase;

  @DeleteMapping("posts/{postId}")
  public ResponseEntity<Object> deletePost(
      @PathVariable @Min(1) Long postId,
      @AuthenticationPrincipal UserDetails userDetails
  ) {
    Long userId = Long.valueOf(userDetails.getUsername());

    postDeleteUseCase.deletePostOne(new PostDeleteCommand(userId, postId));

    return ResponseEntity.noContent().build();
  }
}
