package com.project.socket.comment.service;

import com.project.socket.comment.exception.CommentNotFoundException;
import com.project.socket.comment.exception.InvalidCommentRelationException;
import com.project.socket.comment.model.Comment;
import com.project.socket.comment.repository.CommentJpaRepository;
import com.project.socket.comment.service.usecase.DeleteCommentCommand;
import com.project.socket.comment.service.usecase.DeleteCommentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteCommentService implements DeleteCommentUseCase {

  private final CommentJpaRepository commentJpaRepository;

  @Transactional
  @Override
  public void accept(DeleteCommentCommand deleteCommentCommand) {
    Comment commentToDelete = findComment(deleteCommentCommand.commentId());

    if (!commentToDelete.validateRelation(
        deleteCommentCommand.userId(),
        deleteCommentCommand.postId())) {
      throw new InvalidCommentRelationException();
    }

    commentToDelete.changeStatusToDeleted();
  }

  private Comment findComment(Long commentId) {
    return commentJpaRepository.findById(commentId)
                               .orElseThrow(() -> new CommentNotFoundException(commentId));
  }
}
