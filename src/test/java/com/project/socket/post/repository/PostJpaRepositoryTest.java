package com.project.socket.post.repository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.project.socket.common.annotation.CustomDataJpaTest;
import com.project.socket.post.model.Post;
import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostType;
import com.project.socket.post.service.usecase.PostDto;
import com.project.socket.user.model.User;
import com.project.socket.user.repository.UserJpaRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@CustomDataJpaTest
class PostJpaRepositoryTest {

  @Autowired
  PostJpaRepository postJpaRepository;

  @Autowired
  UserJpaRepository userJpaRepository;

  @Test
  @Sql("findById.sql")
  void id에_해당하는_포스트가_있다면_포스트가_담긴_Optional_객체를_반환한다() {
    Optional<Post> foundPost = postJpaRepository.findById(1L);

    assertThat(foundPost).isPresent();
  }

  @Test
  void id에_해당하는_포스트가_없다면_빈_Optional_객체를_반환한다() {
    Optional<Post> foundPost = postJpaRepository.findById(1L);

    assertThat(foundPost).isNotPresent();
  }


  @Sql("savePost.sql")
  @Test
  void Post_엔티티_저장한다() {
    Optional<User> findUser = userJpaRepository.findById(1L);

    Post post = Post.builder()
        .user(findUser.get())
        .title("aaaa")
        .postContent("안녕하세요")
        .postType(PostType.PROJECT)
        .postMeeting(PostMeeting.ONLINE)
        .build();

    Post savedPost = postJpaRepository.save(post);

    assertAll(
        () -> assertThat(savedPost.getId()).isNotNull(),
        () -> assertThat(savedPost.getTitle()).isNotNull(),
        () -> assertThat(savedPost.getPostContent()).isNotNull(),
        () -> assertThat(savedPost.getPostType()).isNotNull(),
        () -> assertThat(savedPost.getPostMeeting()).isNotNull()
    );
  }

  @Sql("findSinglePostByPostId.sql")
  @Test
  void postId에_해당하는_게시글들을_반환한다() {
    Optional<PostDto> postByPostId = postJpaRepository.findPostByPostId(1L);

    assertThat(postByPostId).isPresent();
  }

  @Test
  void postId에_해당하는_게시물이_없으면_빈_Optional_객체를_반환한다() {
    Optional<PostDto> postByPostId = postJpaRepository.findPostByPostId(1L);

    assertThat(postByPostId).isNotPresent();
  }

  @Sql("findSinglePostByPostId.sql")
  @Test
  void postId가_null이면_게시글을_반환한다() {
    Optional<PostDto> postByPostId = postJpaRepository.findPostByPostId(null);
    
    assertThat(postByPostId).isNotEmpty();
  }
}
