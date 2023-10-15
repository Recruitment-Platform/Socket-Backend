/*
package com.project.socket.post.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.socket.user.model.User;
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

  @Test
  void 게시물_등록_테스트() {
    User user = User.builder().userId(1L).build();
    final String title = "안녕하세요";
    final String postContent = "socket-backend 입니다.";
    final PostType postType = PostType.PROJECT;
    final PostMeeting postMeeting = PostMeeting.ONLINE;

    Post findPost = Post.createNewPost(user, title, postContent, postType, postMeeting);

    assertThat(findPost).satisfies(post -> {
          assertThat(post.getTitle()).isEqualTo(title);
          assertThat(post.getPostContent()).isEqualTo(postContent);
          assertThat(post.getPostType()).isEqualTo(postType);
          assertThat(post.getPostMeeting()).isEqualTo(postMeeting);
        }
    );


  }


}*/
