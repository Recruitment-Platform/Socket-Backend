package com.project.socket.post.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.socket.common.annotation.CustomDataJpaTest;
import com.project.socket.post.model.Post;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@CustomDataJpaTest
class PostJpaRepositoryTest {

  @Autowired
  PostJpaRepository postJpaRepository;

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
}