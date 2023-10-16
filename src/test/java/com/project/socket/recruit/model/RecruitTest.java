package com.project.socket.recruit.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.socket.recruit.model.Recruit;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class RecruitTest {

  @Test
  void Recruit_엔티티_생성() {
    Recruit recruit = Recruit.builder().id(1L).build();
    assertThat(recruit).isNotNull();
  }
}