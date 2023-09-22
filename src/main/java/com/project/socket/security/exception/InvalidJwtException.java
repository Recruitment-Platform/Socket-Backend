package com.project.socket.security.exception;

import com.project.socket.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidJwtException extends RuntimeException{
  private ErrorCode errorCode;

  public InvalidJwtException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }
}
