package com.project.socket.post.controller;

import com.project.socket.post.controller.dto.request.PostSaveRequestDto;
import com.project.socket.post.service.PostSaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Validated
public class PostSaveController {

    private final PostSaveService postSaveService;

    @PostMapping("/posts")
    public ResponseEntity<Object> postSave(@RequestBody @Valid PostSaveRequestDto postSaveRequestDto,
                                           @AuthenticationPrincipal UserDetails userDetails) { //사용자 입력 정보

        long userId = Long.parseLong(userDetails.getUsername());

        Long savedPost = postSaveService.createPost(postSaveRequestDto.toSave(userId));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost)
                .toUri();

        return ResponseEntity.created(location).build();

    }
}
