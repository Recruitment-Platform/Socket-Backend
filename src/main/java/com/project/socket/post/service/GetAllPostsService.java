package com.project.socket.post.service;

import com.project.socket.post.exception.PostNotFoundException;
import com.project.socket.post.model.PostStatus;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.post.service.usecase.GetAllPostsUseCase;
import com.project.socket.post.service.usecase.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GetAllPostsService implements GetAllPostsUseCase {

  private final PostJpaRepository postJpaRepository;

  @Override
  @Transactional(readOnly = true)
  public PostDto getSinglePost(Long postId) {
    PostDto postByPostId = postJpaRepository.findPostByPostId(postId)
        .orElseThrow(IllegalArgumentException::new);
    
    if (isPostDeleted(postByPostId)) {
      throw new PostNotFoundException(postId);
    }

    return postByPostId;
  }

  private boolean isPostDeleted(PostDto postByPostId) {
    return postByPostId.getPostStatus().equals(PostStatus.DELETED);
  }


}
