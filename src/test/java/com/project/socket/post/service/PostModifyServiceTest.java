package com.project.socket.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.project.socket.post.exception.InvalidPostRelationException;
import com.project.socket.post.exception.PostNotFoundException;
import com.project.socket.post.model.Post;
import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostStatus;
import com.project.socket.post.model.PostType;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.post.service.usecase.PostModifyCommand;
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
class PostModifyServiceTest {

  @InjectMocks
  PostModifyService postModifyService;

  @Mock
  PostJpaRepository postJpaRepository;

  PostModifyCommand command = new PostModifyCommand(1L, 1L, "modifyTitle", "modifyContent",
      PostType.PROJECT, PostMeeting.ONLINE);

  @Test
  void id에_해당하는_post가_없으면_PostNotFoundException_예외가_발생한다() {
    when(postJpaRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> postModifyService.modifyPostNew(command))
        .isInstanceOf(PostNotFoundException.class);
  }

  @Test
  void 수정할_post의_정보와_요청의_연관관계_검증이_실패하면_InvalidPostRelationException_예외가_발생한다() {
    Post givenPost = Post.builder().id(1L).title("title").postContent("content")
        .postMeeting(PostMeeting.ONLINE).postMeeting(PostMeeting.ONLINE)
        .user(User.builder().userId(2L).build()).build();

    when(postJpaRepository.findById(anyLong())).thenReturn(Optional.of(givenPost));

    assertThatThrownBy(() -> postModifyService.modifyPostNew(command))
        .isInstanceOf(InvalidPostRelationException.class);
  }

  @Test
  void 요청이_유효하면_성공적으로_변경정보를_반영한다() {
    Post givenPost = Post.builder().id(1L).title("prevTitle").postContent("prevContent")
        .postType(PostType.STUDY).postMeeting(PostMeeting.OFFLINE)
        .postStatus(PostStatus.CREATED)
        .user(User.builder().userId(1L).build()).build();

    when(postJpaRepository.findById(anyLong())).thenReturn(Optional.of(givenPost));

    Post modifiedPost = postModifyService.modifyPostNew(command);

    assertAll(
        () -> assertThat(modifiedPost.getTitle()).isEqualTo(command.title()),
        () -> assertThat(modifiedPost.getPostContent()).isEqualTo(command.postContent()),
        () -> assertThat(modifiedPost.getPostType()).isEqualTo(command.postType()),
        () -> assertThat(modifiedPost.getPostMeeting()).isEqualTo(command.postMeeting()),
        () -> assertThat(modifiedPost.getPostStatus()).isEqualTo(PostStatus.MODIFIED)
    );
  }

}
