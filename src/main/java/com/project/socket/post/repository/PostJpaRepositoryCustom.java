package com.project.socket.post.repository;

import com.project.socket.post.service.usecase.PostDto;
import java.util.Optional;

public interface PostJpaRepositoryCustom {

  Optional<PostDto> findPostByPostId(Long postId);
}
