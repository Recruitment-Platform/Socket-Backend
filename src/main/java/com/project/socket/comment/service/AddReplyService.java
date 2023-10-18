package com.project.socket.comment.service;

import com.project.socket.comment.exception.CommentNotFoundException;
import com.project.socket.comment.model.Comment;
import com.project.socket.comment.repository.CommentJpaRepository;
import com.project.socket.comment.service.usecase.AddReplyCommand;
import com.project.socket.comment.service.usecase.AddReplyUseCase;
import com.project.socket.post.exception.PostNotFoundException;
import com.project.socket.post.model.Post;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.user.exception.UserNotFoundException;
import com.project.socket.user.model.User;
import com.project.socket.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddReplyService implements AddReplyUseCase {

  private final CommentJpaRepository commentJpaRepository;
  private final UserJpaRepository userJpaRepository;
  private final PostJpaRepository postJpaRepository;

  @Transactional
  @Override
  public Comment apply(AddReplyCommand addReplyCommand) {
    User writer = findUser(addReplyCommand.userId());
    Post postToAddComment = findPost(addReplyCommand.postId());
    Comment parentComment = findParentComment(addReplyCommand.parentId());

    Comment reply = Comment
        .createNewComment(postToAddComment, writer, addReplyCommand.content());

    reply.setupParentComment(parentComment);

    return commentJpaRepository.save(reply);
  }

  private User findUser(Long userId) {
    return userJpaRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
  }

  private Post findPost(Long postId) {
    return postJpaRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
  }

  private Comment findParentComment(Long commentId) {
    return commentJpaRepository.findById(commentId)
                               .orElseThrow(() -> new CommentNotFoundException(commentId));
  }
}
