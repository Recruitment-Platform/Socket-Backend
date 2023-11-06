package com.project.socket.post.controller;

import com.project.socket.post.controller.dto.request.PostSaveRequestDto;
import com.project.socket.post.model.Post;
import com.project.socket.post.service.usecase.PostSaveUseCase;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@Validated
public class PostSaveController {

  private final PostSaveUseCase PostSaveUseCase;

  @PostMapping("/posts")
  public ResponseEntity<Object> postSave(
      @RequestBody @Validated PostSaveRequestDto postSaveRequestDto,
      @AuthenticationPrincipal UserDetails userDetails) { //사용자 입력 정보

    long userId = Long.parseLong(userDetails.getUsername());

    Post savedPost = PostSaveUseCase.createPost(
        postSaveRequestDto.toCommand(userId));

    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(savedPost.getId())
        .toUri();

    return ResponseEntity.created(location).build();

  }
}
