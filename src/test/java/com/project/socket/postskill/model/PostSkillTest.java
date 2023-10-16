package com.project.socket.postskill.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.socket.postskill.model.PostSkill;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class PostSkillTest {

  @Test
  void postSkill_엔티티_생성() {
    PostSkill postSkill = PostSkill.builder().id(1L).build();
    assertThat(postSkill).isNotNull();
  }
}