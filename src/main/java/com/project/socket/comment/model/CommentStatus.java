package com.project.socket.comment.model;

import com.project.socket.common.model.EnumType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CommentStatus implements EnumType {
  CREATED("생성"),
  MODIFIED("수정"),
  DELETED("삭제");

  private final String description;
  @Override
  public String getName() {
    return this.description;
  }

  @Override
  public String getDescription() {
    return this.name();
  }
}
