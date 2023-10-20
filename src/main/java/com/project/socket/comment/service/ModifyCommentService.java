package com.project.socket.comment.service;

import com.project.socket.comment.exception.CommentNotFoundException;
import com.project.socket.comment.exception.InvalidCommentRelationException;
import com.project.socket.comment.model.Comment;
import com.project.socket.comment.repository.CommentJpaRepository;
import com.project.socket.comment.service.usecase.ModifyCommentCommand;
import com.project.socket.comment.service.usecase.ModifyCommentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ModifyCommentService implements ModifyCommentUseCase {

  private final CommentJpaRepository commentJpaRepository;

  @Transactional
  @Override
  public Comment apply(ModifyCommentCommand modifyCommentCommand) {
    Comment commentToModify = findComment(modifyCommentCommand.commentId());

    if (!commentToModify.validateRelation(
        modifyCommentCommand.userId(),
        modifyCommentCommand.postId())) {
      throw new InvalidCommentRelationException();
    }

    commentToModify.modifyContent(modifyCommentCommand.content());
    return commentToModify;
  }

  private Comment findComment(Long commentId) {
    return commentJpaRepository.findById(commentId)
                               .orElseThrow(() -> new CommentNotFoundException(commentId));
  }
}
