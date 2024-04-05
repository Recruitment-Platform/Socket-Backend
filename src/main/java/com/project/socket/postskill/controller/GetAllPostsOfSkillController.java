package com.project.socket.postskill.controller;

import com.project.socket.post.controller.dto.response.PagePostResponseDto;
import com.project.socket.post.service.usecase.PostDto;
import com.project.socket.postskill.service.usecase.GetAllPostsOfSkillUseCase;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class GetAllPostsOfSkillController {

  private final GetAllPostsOfSkillUseCase getAllPostsOfSkillUseCase;

  @GetMapping("/posts/projects")
  public ResponseEntity<Object> getAllPostsByHashTag(
      @RequestParam(required = false) String hashtag,
      @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
      @RequestParam(name = "order", defaultValue = "createdAt") String order
  ) {

    int size = 5;

    Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, order);

    Page<PostDto> postsUsingSkill =
        getAllPostsOfSkillUseCase.getPostsUsingSkill(hashtag, pageable);

    PagePostResponseDto responseDto = PagePostResponseDto.toResponse(postsUsingSkill);

    return ResponseEntity.ok(responseDto);
  }
}
