package com.project.socket.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.socket.post.model.Post;
import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostStatus;
import com.project.socket.post.model.PostType;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.post.service.usecase.PostSaveCommand;
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
class PostSaveServiceTest {

  @InjectMocks
  PostSaveService postSaveService;

  @Mock
  PostJpaRepository postJpaRepository;

  @Mock
  UserJpaRepository userJpaRepository;

  @Captor
  ArgumentCaptor<Post> postCaptor;


  PostSaveCommand createPost() {
    return new PostSaveCommand("테스트 제목", "테스트 내용", PostType.PROJECT, PostMeeting.ONLINE, 1L);
  }

  @Test
  void id에_해당하는_유저가_없으면_UserNotFoundException_예외가_발생한다() {
    PostSaveCommand postNew = createPost();
    when(userJpaRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertAll(
        () -> assertThatThrownBy(() -> postSaveService.createPost(postNew)).isInstanceOf(
            UserNotFoundException.class),
        () -> verify(postJpaRepository, never()).findById(anyLong())
    );
  }

  @Test
  void 유저가_존재하면_Post를_생성한다() {
    PostSaveCommand postNew = createPost();
    User writer = User.builder().userId(1L).build();

    Post post = Post.builder().id(1L)
        .title(postNew.title())
        .postContent(postNew.postContent())
        .postType(postNew.postType())
        .postMeeting(postNew.postMeeting())
        .build();

    when(userJpaRepository.findById(anyLong())).thenReturn(Optional.of(writer));
    when(postJpaRepository.save(any())).thenReturn(post);

    Post savedPost = postSaveService.createPost(postNew);

    verify(postJpaRepository).save(postCaptor.capture());
    Post capturePost = postCaptor.getValue();

    assertAll(
        () -> assertThat(savedPost.getId()).isEqualTo(1L),
        () -> assertThat(capturePost.getPostStatus()).isEqualTo(PostStatus.CREATED)
    );
  }
}
