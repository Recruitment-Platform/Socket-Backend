package com.project.socket.comment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.socket.comment.model.Comment;
import com.project.socket.comment.service.usecase.CommentOfPostDto;
import com.project.socket.common.annotation.CustomDataJpaTest;
import com.project.socket.post.model.Post;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.user.model.User;
import com.project.socket.user.repository.UserJpaRepository;
import java.util.List;
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

  final String DELETED_CONTENT = "삭제된 댓글입니다";

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

  @Test
  @Sql("findById.sql")
  void id에_해당하는_댓글이_있다면_댓글이_담긴_Optional_객체를_반환한다() {
    Optional<Comment> foundComment = commentJpaRepository.findById(1L);

    assertThat(foundComment).isPresent();
  }

  @Test
  void id에_해당하는_댓글이_없다면_빈_Optional_객체를_반환한다() {
    Optional<Comment> foundComment = commentJpaRepository.findById(1L);

    assertThat(foundComment).isNotPresent();
  }

  @Test
  @Sql("findAllCommentsByPostId.sql")
  void postId에_해당하는_댓글들을_반환한다() {
    List<CommentOfPostDto> allCommentByPostId = commentJpaRepository.findAllCommentsByPostId(1L);

    assertThat(allCommentByPostId).hasSize(8);
  }

  @Test
  void postId에_해당하는_댓글이_없으면_빈_리스트를_반환한다() {
    List<CommentOfPostDto> allCommentByPostId = commentJpaRepository.findAllCommentsByPostId(1L);

    assertThat(allCommentByPostId).isEmpty();
  }

  @Test
  @Sql("findAllCommentsByPostId.sql")
  void postId가_null_이면_모든_댓글을_반환한다() {
    List<CommentOfPostDto> allCommentByPostId = commentJpaRepository.findAllCommentsByPostId(null);

    assertThat(allCommentByPostId).isNotEmpty();
  }

  @Test
  @Sql("findAllCommentsByPostId.sql")
  void 삭제된_댓글이면_content를_변경해_반환한다() {
    List<CommentOfPostDto> allCommentByPostId = commentJpaRepository.findAllCommentsByPostId(1L);

    assertThat(allCommentByPostId)
        .element(2)
        .satisfies(comment -> assertThat(comment.getContent()).isEqualTo(DELETED_CONTENT));
  }

}