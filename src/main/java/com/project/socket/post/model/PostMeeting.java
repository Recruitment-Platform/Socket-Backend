package com.project.socket.post.model;

import com.project.socket.common.model.EnumType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PostMeeting implements EnumType {
  ONLINE("온라인"),
  OFFLINE("오프라인"),
  ON_OFFLINE("온_오프라인");

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
