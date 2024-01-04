package com.project.socket.post.controller;


import com.project.socket.post.controller.dto.response.PostResponseDto;
import com.project.socket.post.service.usecase.GetSinglePostUseCase;
import com.project.socket.post.service.usecase.PostDto;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class GetSinglePostController {

  private final GetSinglePostUseCase getSinglePostUseCase;

  @GetMapping("/posts/{postId}")
  public ResponseEntity<Object> getOnePost(@PathVariable @Min(1) Long postId) {
    PostDto postsMap = getSinglePostUseCase.getPostDetail(postId);

    PostResponseDto responseDto = PostResponseDto.toResponse(postsMap);

    return ResponseEntity.ok(responseDto);
  }
}
