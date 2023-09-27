package com.project.socket.common.error;

public enum ProblemDetailFieldDescription {
  TYPE("type", "문제 유형을 식별하는 URI 참고"),
  TITLE("title", "문제 유형"),
  STATUS("status", "Http status 코드"),
  DETAIL("detail", "문제 유형 설명"),
  INSTANCE("instance", "문제가 발생한 URI"),
  CODE("code", "팀에서 정의한 에러코드"),
  DETAIL_LIST_FIELD("detail[0].field", "문제있는 필드"),
  DETAIL_LIST_VALUE("detail[0].value", "문제있는 값"),
  DETAIL_LIST_REASON("detail[0].reason", "문제 원인");

  private String field;
  private String description;

  ProblemDetailFieldDescription(String field, String description) {
    this.field = field;
    this.description = description;
  }

  public String getField() {
    return field;
  }

  public String getDescription() {
    return description;
  }
}
