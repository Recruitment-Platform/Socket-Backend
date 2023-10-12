package com.project.socket.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.socket.comment.model.Comment;
import com.project.socket.comment.model.CommentStatus;
import com.project.socket.comment.repository.CommentJpaRepository;
import com.project.socket.comment.service.usecase.AddCommentCommand;
import com.project.socket.post.exception.PostNotFoundException;
import com.project.socket.post.model.Post;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.user.exception.UserNotFoundException;
import com.project.socket.user.model.User;
import com.project.socket.user.repository.UserJpaRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
class AddCommentServiceTest {

  @InjectMocks
  AddCommentService addCommentService;

  @Mock
  CommentJpaRepository commentJpaRepository;

  @Mock
  UserJpaRepository userJpaRepository;

  @Mock
  PostJpaRepository postJpaRepository;

  @Captor
  ArgumentCaptor<Comment> commentCaptor;

  @Test
  void id에_해당하는_유저가_없으면_UserNotFoundException_예외가_발생한다() {
    AddCommentCommand command = createCommand();
    when(userJpaRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertAll(
        () -> assertThatThrownBy(() -> addCommentService.apply(command))
            .isInstanceOf(UserNotFoundException.class),
        () -> verify(postJpaRepository, never()).findById(anyLong()),
        () -> verify(commentJpaRepository, never()).save(any())
    );
  }

  @Test
  void id에_해당하는_포스트가_없으면_PostNotFoundException_예외가_발생한다() {
    AddCommentCommand command = createCommand();
    User writer = User.builder().userId(1L).build();

    when(userJpaRepository.findById(anyLong())).thenReturn(Optional.of(writer));
    when(postJpaRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertAll(
        () -> assertThatThrownBy(() -> addCommentService.apply(command))
            .isInstanceOf(PostNotFoundException.class),
        () -> verify(commentJpaRepository, never()).save(any())
    );
  }

  @Test
  void 유저와_포스트가_모두_존재하면_Comment를_추가한다() {
    AddCommentCommand command = createCommand();
    User writer = User.builder().userId(1L).build();
    Post postToSaveComment = Post.builder().id(1L).build();
    Comment comment = Comment.builder()
                             .id(1L)
                             .content(command.content()).user(writer).cPost(postToSaveComment)
                             .build();

    when(userJpaRepository.findById(anyLong())).thenReturn(Optional.of(writer));
    when(postJpaRepository.findById(anyLong())).thenReturn(Optional.of(postToSaveComment));
    when(commentJpaRepository.save(any())).thenReturn(comment);

    Comment savedComment = addCommentService.apply(command);
    // save 메서드에 사용된 comment 인자 테스트를 위해 capture
    verify(commentJpaRepository).save(commentCaptor.capture());
    Comment captureComment = commentCaptor.getValue();

    assertAll(
        () -> assertThat(savedComment.getId()).isEqualTo(1L),
        () -> assertThat(captureComment.getCommentStatus()).isEqualTo(CommentStatus.CREATED)
    );

  }

  AddCommentCommand createCommand() {
    return new AddCommentCommand("content", 1L, 1L);
  }
}