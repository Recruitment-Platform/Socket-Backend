package com.project.socket.comment.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.socket.post.model.Post;
import com.project.socket.user.model.User;
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

  @Test
  void 댓글을_추가할_새로운_Comment를_반환한다() {
    Post post = Post.builder().id(1L).build();
    User user = User.builder().userId(1L).build();
    final String CONTENT = "content";

    Comment newComment = Comment.createNewComment(post, user, CONTENT);

    assertThat(newComment).satisfies(comment -> {
          assertThat(comment.getContent()).isEqualTo(CONTENT);
          assertThat(comment.getCommentStatus()).isEqualTo(CommentStatus.CREATED);
          assertThat(comment.getParentComment()).isNull();
        }
    );
  }
}