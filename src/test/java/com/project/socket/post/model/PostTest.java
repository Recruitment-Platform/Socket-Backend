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

  @Test
  void 유효한_연관관계이면_true를_반환한다() {
    User user = User.builder().userId(1L).build();
    Post post = Post.builder().user(user).build();

    assertThat(post.isValidPostRelation(1L)).isTrue();
  }

  @Test
  void 유효한_연관관계가_아니면_false를_반환한다() {
    User user = User.builder().userId(1L).build();
    Post post = Post.builder().user(user).build();

    assertThat(post.isValidPostRelation(2L)).isFalse();
  }

  @Test
  void post의_title을_수정하고_status를_MODIFIED로_변경한다() {
    Post samplePost = Post.builder()
        .postContent("title")
        .postStatus(PostStatus.CREATED)
        .build();

    Post modifyTitle = Post.builder()
        .title("modify title")
        .build();

    samplePost.modifyInfo(modifyTitle);

    assertThat(samplePost).satisfies(post -> {
      assertThat(post.getTitle()).isEqualTo("modify title");
      assertThat(post.getPostStatus()).isEqualTo(PostStatus.MODIFIED);
    });
  }

  @Test
  void post의_postContent를_수정하고_status를_MODIFIED로_변경한다() {
    Post samplePost = Post.builder()
        .postContent("content")
        .postStatus(PostStatus.CREATED)
        .build();

    Post modifyPostContent = Post.builder()
        .postContent("modify content")
        .build();

    samplePost.modifyInfo(modifyPostContent);

    assertThat(samplePost).satisfies(post -> {
      assertThat(post.getPostContent()).isEqualTo("modify content");
      assertThat(post.getPostStatus()).isEqualTo(PostStatus.MODIFIED);
    });
  }

  @Test
  void post의_postType을_수정하고_status를_MODIFIED로_변경한다() {
    Post samplePost = Post.builder()
        .postType(PostType.STUDY)
        .postStatus(PostStatus.CREATED)
        .build();

    Post modifyPostType = Post.builder()
        .postType(PostType.PROJECT)
        .build();

    samplePost.modifyInfo(modifyPostType);

    assertThat(samplePost).satisfies(post -> {
      assertThat(post.getPostType()).isEqualTo(PostType.PROJECT);
      assertThat(post.getPostStatus()).isEqualTo(PostStatus.MODIFIED);
    });
  }

  @Test
  void post의_postMeeting을_수정하고_status를_MODIFIED로_변경한다() {
    Post samplePost = Post.builder()
        .postMeeting(PostMeeting.ONLINE)
        .postStatus(PostStatus.CREATED)
        .build();

    Post modifyPostMeeting = Post.builder()
        .postMeeting(PostMeeting.OFFLINE)
        .build();

    samplePost.modifyInfo(modifyPostMeeting);

    assertThat(samplePost).satisfies(post -> {
      assertThat(post.getPostMeeting()).isEqualTo(PostMeeting.OFFLINE);
      assertThat(post.getPostStatus()).isEqualTo(PostStatus.MODIFIED);
    });
  }


}
