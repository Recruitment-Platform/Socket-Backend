package com.project.socket.post.controller;

import com.project.socket.post.controller.dto.request.PostSaveRequestDto;
import com.project.socket.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @PostMapping("/posts")
  public Long save(@RequestBody PostSaveRequestDto requestDto) {
    return postService.save(requestDto);
  }
}
