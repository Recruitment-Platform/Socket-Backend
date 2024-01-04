package com.project.socket.post.model;

import com.project.socket.common.model.EnumType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PostType implements EnumType {
  PROJECT("프로젝트"),
  STUDY("스터디");

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
