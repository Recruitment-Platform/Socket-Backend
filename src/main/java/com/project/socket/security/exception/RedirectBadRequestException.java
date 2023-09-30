package com.project.socket.security.exception;

import com.project.socket.common.error.ErrorCode;
import org.springframework.security.core.AuthenticationException;

public class RedirectBadRequestException extends AuthenticationException {

  public RedirectBadRequestException(ErrorCode errorCode) {
    super(errorCode.getMessage());
  }
}
