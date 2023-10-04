package com.project.socket.user.exception;

import com.project.socket.common.error.ErrorCode;
import com.project.socket.common.error.exception.BusinessException;

public class DuplicatedNicknameException extends BusinessException {

  public DuplicatedNicknameException() {
    super(ErrorCode.DUPLICATED_NICKNAME);
  }
}
