package com.project.socket.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.project.socket.comment.exception.CommentNotFoundException;
import com.project.socket.comment.exception.InvalidCommentRelationException;
import com.project.socket.comment.model.Comment;
import com.project.socket.comment.model.CommentStatus;
import com.project.socket.comment.repository.CommentJpaRepository;
import com.project.socket.comment.service.usecase.DeleteCommentCommand;
import com.project.socket.post.model.Post;
import com.project.socket.user.model.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
class DeleteCommentServiceTest {

  @InjectMocks
  DeleteCommentService deleteCommentService;

  @Mock
  CommentJpaRepository commentJpaRepository;

  DeleteCommentCommand command = new DeleteCommentCommand(1L, 1L, 1L);

  @Test
  void id에_해당하는_comment가_없으면_CommentNotFoundException_예외가_발생한다() {
    when(commentJpaRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> deleteCommentService.accept(command))
        .isInstanceOf(CommentNotFoundException.class);
  }

  @Test
  void 요청정보와_comment의_연관관계가_일치하지_않으면_InvalidCommentRelationException_예외가_발생한다() {
    Comment comment = Comment.builder().id(1L).content("previous")
                             .cPost(Post.builder().id(1L).build())
                             .writer(User.builder().userId(2L).build()).build();
    when(commentJpaRepository.findById(anyLong())).thenReturn(Optional.of(comment));

    assertThatThrownBy(() -> deleteCommentService.accept(command))
        .isInstanceOf(InvalidCommentRelationException.class);
  }

  @Test
  void 요청이_유효하면_성공적으로_DELETED로_status를_변경한다() {
    Comment comment = Comment.builder().id(1L).content("previous")
                             .cPost(Post.builder().id(1L).build())
                             .commentStatus(CommentStatus.CREATED)
                             .writer(User.builder().userId(1L).build()).build();
    when(commentJpaRepository.findById(anyLong())).thenReturn(Optional.of(comment));

    deleteCommentService.accept(command);

    assertThat(comment.getCommentStatus()).isEqualTo(CommentStatus.DELETED);
  }
}