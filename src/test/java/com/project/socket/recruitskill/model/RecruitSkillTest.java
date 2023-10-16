package com.project.socket.recruitskill.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.socket.recruitskill.model.RecruitSkill;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class RecruitSkillTest {

  @Test
  void RecruitSkill_엔티티_생성() {
    RecruitSkill recruitSkill = RecruitSkill.builder().id(1L).build();
    assertThat(recruitSkill).isNotNull();
  }
}