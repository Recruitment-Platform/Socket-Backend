package com.project.socket.post.exception;

import com.project.socket.common.error.ErrorCode;
import com.project.socket.common.error.exception.BusinessException;

public class UnsupportedSortException extends BusinessException {

  public UnsupportedSortException() {
    super(ErrorCode.UnsupportedSortException);
  }
}
