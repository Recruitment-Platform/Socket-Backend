package com.project.socket.post.service;

import com.project.socket.post.exception.InvalidPostRelationException;
import com.project.socket.post.exception.PostNotFoundException;
import com.project.socket.post.model.Post;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.post.service.usecase.PostModifyCommand;
import com.project.socket.post.service.usecase.PostModifyUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PostModifyService implements PostModifyUseCase {

  private final PostJpaRepository postJpaRepository;


  @Transactional
  @Override
  public Post modifyPostNew(PostModifyCommand postModifyCommand) {
    Post postToModify = findPost(postModifyCommand.postId());

    if (!postToModify.isValidPostRelation(
        postModifyCommand.userId())) {
      throw new InvalidPostRelationException();
    }

    postToModify.modifyInfo(postModifyCommand.toModifiedEntity());
    return postToModify;
  }

  private Post findPost(Long postId) {
    return postJpaRepository.findById(postId)
        .orElseThrow(() -> new PostNotFoundException(postId));
  }
}
