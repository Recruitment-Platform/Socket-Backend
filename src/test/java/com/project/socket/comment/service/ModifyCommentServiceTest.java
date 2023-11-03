package com.project.socket.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.project.socket.comment.exception.CommentNotFoundException;
import com.project.socket.comment.exception.InvalidCommentRelationException;
import com.project.socket.comment.model.Comment;
import com.project.socket.comment.model.CommentStatus;
import com.project.socket.comment.repository.CommentJpaRepository;
import com.project.socket.comment.service.usecase.ModifyCommentCommand;
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
class ModifyCommentServiceTest {

  @InjectMocks
  ModifyCommentService modifyCommentService;

  @Mock
  CommentJpaRepository commentJpaRepository;

  ModifyCommentCommand command = new ModifyCommentCommand(1L, 1L, 1L, "modify");

  @Test
  void id에_해당하는_comment가_없으면_CommentNotFoundException_예외가_발생한다() {
    when(commentJpaRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> modifyCommentService.apply(command))
        .isInstanceOf(CommentNotFoundException.class);
  }

  @Test
  void 수정할_comment의_정보와_요청의_연관관계_검증이_실패하면_InvalidCommentRelationException_예외가_발생한다() {
    Comment givenComment = Comment.builder().id(1L).content("previous")
                                  .cPost(Post.builder().id(1L).build())
                                  .writer(User.builder().userId(2L).build()).build();
    when(commentJpaRepository.findById(anyLong())).thenReturn(Optional.of(givenComment));

    assertThatThrownBy(() -> modifyCommentService.apply(command))
        .isInstanceOf(InvalidCommentRelationException.class);
  }

  @Test
  void 요청이_유효하면_성공적으로_content를_변경한다() {
    Comment givenComment = Comment.builder().id(1L).content("previous")
                                  .cPost(Post.builder().id(1L).build())
                                  .commentStatus(CommentStatus.CREATED)
                                  .writer(User.builder().userId(1L).build()).build();
    when(commentJpaRepository.findById(anyLong())).thenReturn(Optional.of(givenComment));

    Comment modifiedComment = modifyCommentService.apply(command);

    assertAll(
        () -> assertThat(modifiedComment.getContent()).isEqualTo(command.content()),
        () -> assertThat(modifiedComment.getCommentStatus()).isEqualTo(CommentStatus.MODIFIED)
    );
  }

}