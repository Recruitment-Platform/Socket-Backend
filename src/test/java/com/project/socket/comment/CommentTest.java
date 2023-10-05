package com.project.socket.comment;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.socket.comment.model.Comment;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;


@DisplayNameGeneration(ReplaceUnderscores.class)
class CommentTest {

  @Test
  void comment_엔티티_생성() {
    Comment comment = Comment.builder().id(1L).build();
    assertThat(comment).isNotNull();
  }
}