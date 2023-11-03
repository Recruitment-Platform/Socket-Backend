package com.project.socket.comment.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.socket.post.model.Post;
import com.project.socket.user.model.User;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


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

  @ParameterizedTest
  @MethodSource("providePostIdAndUserId")
  void 유효한_연관관계가_아니면_false를_반환한다(Long postId, Long userId){
    Post post = Post.builder().id(1L).build();
    User user = User.builder().userId(1L).build();
    Comment comment = Comment.builder().writer(user).cPost(post).build();

    assertThat(comment.validateRelation(userId, postId)).isFalse();
  }

  @Test
  void 유효한_연관관계라면_true를_반환한다(){
    Post post = Post.builder().id(1L).build();
    User user = User.builder().userId(1L).build();
    Comment comment = Comment.builder().writer(user).cPost(post).build();

    assertThat(comment.validateRelation(1L, 1L)).isTrue();
  }

  private static Stream<Arguments> providePostIdAndUserId(){
    return Stream.of(
        Arguments.of(1L, 2L),
        Arguments.of(2L, 2L),
        Arguments.of(2L, 1L)
    );
  }
}