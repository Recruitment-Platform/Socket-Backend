package com.project.socket.post.service;

import com.project.socket.post.exception.InvalidPostRelationException;
import com.project.socket.post.exception.PostNotFoundException;
import com.project.socket.post.model.Post;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.post.service.usecase.PostDeleteCommand;
import com.project.socket.post.service.usecase.PostDeleteUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PostDeleteService implements PostDeleteUseCase {

  private final PostJpaRepository postJpaRepository;

  @Transactional
  @Override
  public void deletePostOne(PostDeleteCommand postDeleteCommand) {
    Post postToDelete = findPost(postDeleteCommand.postId());

    if (!postToDelete.isWriter(
        postDeleteCommand.userId())) {
      throw new InvalidPostRelationException();
    }

    postToDelete.changeStatusToDeleted();
  }

  private Post findPost(Long postId) {
    return postJpaRepository.findById(postId)
        .orElseThrow(() -> new PostNotFoundException(postId));
  }
}
