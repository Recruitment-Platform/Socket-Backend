package com.project.socket.postrecruit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class PostRecruitTest {

  @Test
  void postRecruit_엔티티_생성() {
    PostRecruit postRecruit = PostRecruit.builder().id(1L).build();
    assertThat(postRecruit).isNotNull();
  }
}