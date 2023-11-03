package com.project.socket.comment.exception;

import static com.project.socket.common.error.ErrorCode.COMMENT_NOT_FOUND;

import com.project.socket.common.error.exception.BusinessException;

public class CommentNotFoundException extends BusinessException {

  public CommentNotFoundException(Long commentId) {
    super("Comment ID: " + commentId, COMMENT_NOT_FOUND);
  }
}
