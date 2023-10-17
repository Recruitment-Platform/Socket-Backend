package com.project.socket.post.service;

import com.project.socket.post.controller.dto.request.PostSaveRequestDto;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final UserJpaRepository userJpaRepository;


    @Transactional
    public Long save(PostSaveRequestDto requestDto) {
        return postJpaRepository.save(requestDto.toEntity()).getId();
    }
}
