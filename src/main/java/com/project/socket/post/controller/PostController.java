package com.project.socket.post.controller;

import com.project.socket.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

}
