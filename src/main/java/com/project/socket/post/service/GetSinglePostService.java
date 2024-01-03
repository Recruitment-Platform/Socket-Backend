package com.project.socket.post.service;

import com.project.socket.post.exception.PostDeletedException;
import com.project.socket.post.exception.PostNotFoundException;
import com.project.socket.post.model.PostStatus;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.post.service.usecase.GetSinglePostUseCase;
import com.project.socket.post.service.usecase.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GetSinglePostService implements GetSinglePostUseCase {

  private final PostJpaRepository postJpaRepository;

  @Override
  @Transactional(readOnly = true)
  public PostDto getPostDetail(Long postId) {
    PostDto postByPostId = postJpaRepository.findPostByPostId(postId)
        .orElseThrow(() -> new PostNotFoundException(postId));

    if (isPostDeleted(postByPostId)) {
      throw new PostDeletedException(postId);
    }

    return postByPostId;
  }

  private boolean isPostDeleted(PostDto postByPostId) {
    return postByPostId.getPostStatus().equals(PostStatus.DELETED);
  }


}
