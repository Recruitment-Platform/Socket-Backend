package com.project.socket.comment.controller;

import com.project.socket.comment.controller.dto.response.CommentOfPostResponseDto;
import com.project.socket.comment.service.usecase.CommentOfPostDto;
import com.project.socket.comment.service.usecase.GetAllCommentsOfPostUseCase;
import jakarta.validation.constraints.Min;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class GetAllCommentsOfPostController {

  private final GetAllCommentsOfPostUseCase getAllCommentsOfPostUseCase;

  @GetMapping("/posts/{postId}/comments")
  public ResponseEntity<Object> getAllCommentsOfPost(@PathVariable @Min(1) Long postId) {
    Map<Long, List<CommentOfPostDto>> commentsMap = getAllCommentsOfPostUseCase.apply(postId);

    List<CommentOfPostResponseDto> responseDtos =
        commentsMap.values().stream().map(CommentOfPostResponseDto::toResponse).toList();

    return ResponseEntity.ok(responseDtos);

  }
}
