package com.project.socket.comment.controller;

import com.project.socket.comment.controller.dto.request.AddCommentRequestDto;
import com.project.socket.comment.model.Comment;
import com.project.socket.comment.service.usecase.AddCommentUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@Validated
public class AddCommentController {

  private final AddCommentUseCase addCommentUseCase;

  @PostMapping("/posts/{postId}/comments")
  public ResponseEntity<Object> addComment(@PathVariable @Min(1) Long postId,
      @RequestBody @Valid AddCommentRequestDto addCommentRequestDto,
      @AuthenticationPrincipal UserDetails userDetails) {
    long userId = Long.parseLong(userDetails.getUsername());

    Comment savedComment = addCommentUseCase.apply(addCommentRequestDto.toCommand(userId, postId));

    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                              .path("/{id}")
                                              .buildAndExpand(savedComment.getId())
                                              .toUri();
    return ResponseEntity.created(location).build();
  }
}
