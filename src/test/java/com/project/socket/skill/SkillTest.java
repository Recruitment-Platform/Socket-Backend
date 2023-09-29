package com.project.socket.skill;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class SkillTest {

  @Test
  void Skill_엔티티_생성() {
    Skill skill = Skill.builder().id(1L).build();
    assertThat(skill).isNotNull();
  }
}