package com.project.socket.post.exception;

import com.project.socket.common.error.ErrorCode;
import com.project.socket.common.error.exception.BusinessException;

public class InvalidPostRelationException extends BusinessException {

  public InvalidPostRelationException() {
    super(ErrorCode.INVALID_POST_RELATION);
  }
}
