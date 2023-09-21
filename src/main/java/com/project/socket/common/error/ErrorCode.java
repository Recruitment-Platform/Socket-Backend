package com.project.socket.common.error;

public enum ErrorCode {

  OAUTH2_LOGIN_FAIL(401, "A1", "로그인 실패"),
  INVALID_JWT(401, "A2", "유효하지 않은 토큰입니다");

  private int status;
  private final String code;
  private final String message;

  ErrorCode(final int status, final String code, final String message) {
    this.status = status;
    this.message = message;
    this.code = code;
  }

  public int getStatus() {
    return status;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
