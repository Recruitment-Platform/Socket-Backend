package com.project.socket.user.exception;

import static com.project.socket.common.error.ErrorCode.USER_NOT_FOUND;

import com.project.socket.common.error.exception.BusinessException;

public class UserNotFoundException extends BusinessException {

  public UserNotFoundException(Long userId) {
    super("User ID: " + userId, USER_NOT_FOUND);
  }
}
