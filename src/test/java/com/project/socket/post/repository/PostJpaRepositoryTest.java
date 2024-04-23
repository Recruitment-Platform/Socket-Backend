package com.project.socket.post.repository;


import static com.project.socket.post.model.QPost.post;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.project.socket.common.annotation.CustomDataJpaTest;
import com.project.socket.post.model.Post;
import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostType;
import com.project.socket.post.service.usecase.PostDto;
import com.project.socket.user.model.User;
import com.project.socket.user.repository.UserJpaRepository;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

  @Sql("getAllPostsByHashTag.sql")
  @Test
  void postId에_해당하는_게시물을_페이징하여_createdAt을_기준_내림차순_으로_반환한다() {
    HashSet<Long> postIdList = new HashSet<>(List.of(1L, 2L, 3L, 4L, 5L));
    Pageable pageable = PageRequest.of(0, 2, Sort.Direction.DESC, "createdAt");
    OrderSpecifier<LocalDateTime> orderSpecifier = new OrderSpecifier<>(Order.DESC, post.createdAt);

    Page<PostDto> postsByHashTag = postJpaRepository.getPostsByHashTag(postIdList, pageable,
        orderSpecifier);

    assertThat(postsByHashTag.getSize()).isEqualTo(2);
    assertThat(postsByHashTag.getTotalPages()).isEqualTo(3);
    assertThat(postsByHashTag).extracting("postId").containsExactly(5L, 4L);
  }


  @Sql("getAllPostsByHashTag.sql")
  @Test
  void 입력받는_postId의_HashSet이_빈_컬렉션일_경우_모든_게시물를_페이징하여_반환한다() {
    HashSet<Long> postIdList = new HashSet<>(Collections.emptyList());
    Pageable pageable = PageRequest.of(2, 2, Sort.by("createdAt").descending());
    OrderSpecifier<LocalDateTime> orderSpecifier = new OrderSpecifier<>(Order.DESC, post.createdAt);

    Page<PostDto> postsByHashTag = postJpaRepository.getPostsByHashTag(postIdList, pageable,
        orderSpecifier);

    assertThat(postsByHashTag.getTotalPages()).isEqualTo(3);
    assertThat(postsByHashTag.getTotalElements()).isEqualTo(5);
    assertThat(postsByHashTag).extracting("postId").containsExactly(1L);


  }
}
