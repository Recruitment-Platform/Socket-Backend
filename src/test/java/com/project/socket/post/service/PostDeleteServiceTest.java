package com.project.socket.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.project.socket.post.exception.InvalidPostRelationException;
import com.project.socket.post.exception.PostNotFoundException;
import com.project.socket.post.model.Post;
import com.project.socket.post.model.PostStatus;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.post.service.usecase.PostDeleteCommand;
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
class PostDeleteServiceTest {

  @InjectMocks
  PostDeleteService postDeleteService;

  @Mock
  PostJpaRepository postJpaRepository;

  PostDeleteCommand command = new PostDeleteCommand(1L, 1L);

  @Test
  void id에_해당하는_post가_없으면_PostNotFoundException_예외가_발생한다() {
    when(postJpaRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> postDeleteService.deletePostOne(command))
        .isInstanceOf(PostNotFoundException.class);
  }

  @Test
  void 요청한_유저의_정보와_post_작성_유저가_일치하지_않으면_InvalidPostRelationException_예외가_발생한다() {
    Post post = Post.builder().id(1L).title("title").postContent("content")
        .user(User.builder().userId(2L).build()).build();

    when(postJpaRepository.findById(anyLong())).thenReturn(Optional.of(post));

    assertThatThrownBy(() -> postDeleteService.deletePostOne(command))
        .isInstanceOf(InvalidPostRelationException.class);
  }

  @Test
  void 요청이_유효하면_성공적으로_postStatus를_DELETED로_변경한다() {
    Post post = Post.builder().id(1L).title("title").postStatus(PostStatus.CREATED)
        .user(User.builder().userId(1L).build()).build();

    when(postJpaRepository.findById(anyLong())).thenReturn(Optional.of(post));

    postDeleteService.deletePostOne(command);

    assertThat(post.getPostStatus()).isEqualTo(PostStatus.DELETED);
  }
}