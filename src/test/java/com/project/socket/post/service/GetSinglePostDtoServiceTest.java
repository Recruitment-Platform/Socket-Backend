package com.project.socket.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.project.socket.post.exception.PostDeletedException;
import com.project.socket.post.exception.PostNotFoundException;
import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostStatus;
import com.project.socket.post.model.PostType;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.post.service.usecase.PostDto;
import java.time.LocalDateTime;
import java.util.List;
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
class GetSinglePostDtoServiceTest {

  final Long POST_ID = 1L;

  @InjectMocks
  GetSinglePostService getSinglePostService;

  @Mock
  PostJpaRepository postJpaRepository;

  @Test
  void 조회결과가_없으면_PostNotFoundException_예외가_발생한다() {
    when(postJpaRepository.findPostByPostId(anyLong())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> getSinglePostService.getPostDetail(POST_ID))
        .isInstanceOf(PostNotFoundException.class);
  }

  @Test
  void 조회결과_postStatus가_DELETED일_경우_PostDeletedException_예외가_발생한다() {
    when(postJpaRepository.findPostByPostId(anyLong())).
        thenReturn(Optional.of(sampleDto().get(0)));

    assertThatThrownBy(() -> getSinglePostService.getPostDetail(POST_ID))
        .isInstanceOf(PostDeletedException.class);
  }

  @Test
  void postId에_해당하는_게시물_있으면_성공적으로_조회한다() {
    when(postJpaRepository.findPostByPostId(anyLong())).
        thenReturn(Optional.of(sampleDto().get(1)));

    PostDto searchPost = getSinglePostService.getPostDetail(POST_ID);

    assertAll(
        () -> assertThat(searchPost.getPostId()).isEqualTo(2L),
        () -> assertThat(searchPost.getTitle()).isEqualTo("title"),
        () -> assertThat(searchPost.getPostContent()).isEqualTo("content"),
        () -> assertThat(searchPost.getPostType()).isEqualTo(PostType.STUDY),
        () -> assertThat(searchPost.getPostMeeting()).isEqualTo(PostMeeting.OFFLINE)
    );
  }

  List<PostDto> sampleDto() {
    return List.of(
        new PostDto(1L, "title", "content", PostType.PROJECT, PostMeeting.ONLINE,
            PostStatus.DELETED,
            1L, "nickname", LocalDateTime.now()),
        new PostDto(2L, "title", "content", PostType.STUDY, PostMeeting.OFFLINE, PostStatus.CREATED,
            1L, "nickname", LocalDateTime.now())
    );
  }

}
