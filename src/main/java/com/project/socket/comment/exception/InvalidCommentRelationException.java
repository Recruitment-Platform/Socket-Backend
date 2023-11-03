package com.project.socket.comment.exception;

import com.project.socket.common.error.ErrorCode;
import com.project.socket.common.error.exception.BusinessException;

public class InvalidCommentRelationException extends BusinessException {

  public InvalidCommentRelationException() {
    super(ErrorCode.INVALID_COMMENT_RELATION);
  }
}
