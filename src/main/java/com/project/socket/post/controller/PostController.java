package com.project.socket.post.controller;

import com.project.socket.post.controller.dto.request.PostSaveRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class PostController {


    @PostMapping("/posts")
    public Long postSave(@RequestBody @Valid PostSaveRequestDto postSaveRequestDto) { //사용자 입력 정보

        return postService.save(postSaveRequestDto);

    }
}
