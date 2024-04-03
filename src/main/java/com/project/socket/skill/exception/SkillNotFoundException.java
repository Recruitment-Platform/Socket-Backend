package com.project.socket.skill.exception;

import static com.project.socket.common.error.ErrorCode.SKILL_NOT_FOUND;

import com.project.socket.common.error.exception.BusinessException;

public class SkillNotFoundException extends BusinessException {

  public SkillNotFoundException() {
    super(SKILL_NOT_FOUND);
  }
}
