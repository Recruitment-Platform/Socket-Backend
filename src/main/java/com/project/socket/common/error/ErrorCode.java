package com.project.socket.common.error;

public enum ErrorCode {

  // Auth Error
  OAUTH2_LOGIN_FAIL(401, "A1", "로그인 실패"),
  INVALID_JWT(401, "A2", "유효하지 않은 토큰입니다"),
  HANDLE_ACCESS_DENIED(403, "A3", "권한이 없습니다"),
  AUTHENTICATION_ENTRY_POINT(401, "A4", "인증이 필요합니다"),
  REDIRECT_BAD_REQUEST(400, "A5", "잘못된 요청입니다"),

  // Common
  INVALID_INPUT_VALUE(400, "C1", "올바르지 않은 입력 값입니다"),
  INTERNAL_SERVER_ERROR(500, "C2", "서버 에러"),

  //User
  USER_NOT_FOUND(404, "U1", "해당 유저가 존재하지 않습니다"),
  DUPLICATED_NICKNAME(400, "U2", "중복된 닉네임입니다"),

  // post
  POST_NOT_FOUND(404, "P1", "해당 포스트가 존재하지 않습니다"),
  INVALID_POST_RELATION(400, "P2", "잘못된 요청 입니다"),
  POST_DELETED_NOT_FOUND(404, "P3", "삭제된 포스트라 존재하지 않습니다"),

  // comment
  COMMENT_NOT_FOUND(404, "CM1", "해당 댓글이 존재하지 않습니다"),
  INVALID_COMMENT_RELATION(400, "CM2", "잘못된 요청입니다"),

  /**
   * ChatRoom 관련 에러 코드
   */
  WRITER_CAN_NOT_START_CHAT(400, "CR1", "작성자는 채팅방을 생성할 수 없습니다"),
  CHAT_ROOM_NOT_FOUND(404, "CR2", "해당 채팅방이 존재하지 않습니다");


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
