package com.project.socket.comment.controller;

import com.project.socket.comment.controller.dto.request.AddReplyRequestDto;
import com.project.socket.comment.model.Comment;
import com.project.socket.comment.service.usecase.AddReplyUseCase;
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
public class AddReplyController {

  private final AddReplyUseCase addReplyUseCase;

  @PostMapping("/posts/{postId}/comments/{commentId}/replies")
  public ResponseEntity<Object> addReply(
      @PathVariable @Min(1) Long postId,
      @PathVariable @Min(1) Long commentId,
      @RequestBody @Valid AddReplyRequestDto addReplyRequestDto,
      @AuthenticationPrincipal UserDetails userDetails
  ) {
    long userId = Long.parseLong(userDetails.getUsername());
    Comment reply = addReplyUseCase.apply(addReplyRequestDto.toCommand(userId, postId, commentId));

    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                              .path("/{id}")
                                              .buildAndExpand(reply.getId())
                                              .toUri();
    return ResponseEntity.created(location).build();
  }
}
