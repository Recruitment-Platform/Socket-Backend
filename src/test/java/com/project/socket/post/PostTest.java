package com.project.socket.post;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.socket.post.model.Post;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class PostTest {

  @Test
  void post_엔티티_생성() {
    Post post = Post.builder().id(1L).build();
    assertThat(post).isNotNull();
  }
}