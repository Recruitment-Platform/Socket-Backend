package com.project.socket.comment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.socket.comment.model.Comment;
import com.project.socket.common.annotation.CustomDataJpaTest;
import com.project.socket.post.model.Post;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.user.model.User;
import com.project.socket.user.repository.UserJpaRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@CustomDataJpaTest
class CommentJpaRepositoryTest {

  @Autowired
  CommentJpaRepository commentJpaRepository;

  @Autowired
  UserJpaRepository userJpaRepository;

  @Autowired
  PostJpaRepository postJpaRepository;

  @Test
  @Sql("saveComment.sql")
  void Comment_엔티티를_저장한다() {
    Optional<User> writer = userJpaRepository.findById(1L);
    Optional<Post> post = postJpaRepository.findById(1L);
    Comment comment = Comment.builder()
                             .writer(writer.get()).cPost(post.get())
                             .content("sdf")
                             .build();

    Comment savedComment = commentJpaRepository.save(comment);
    assertThat(savedComment).satisfies(c -> {
      assertThat(c.getId()).isNotNull();
      assertThat(c.getParentComment()).isNull();
    });
  }
}