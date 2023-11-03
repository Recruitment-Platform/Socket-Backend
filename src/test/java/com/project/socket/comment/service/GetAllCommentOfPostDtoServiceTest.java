package com.project.socket.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.project.socket.comment.model.CommentStatus;
import com.project.socket.comment.repository.CommentJpaRepository;
import com.project.socket.comment.service.usecase.CommentOfPostDto;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
class GetAllCommentOfPostDtoServiceTest {

  @InjectMocks
  GetAllCommentsOfPostService getAllCommentsOfPostService;

  @Mock
  CommentJpaRepository commentJpaRepository;

  final Long POST_ID = 1L;

  @Test
  void 조회결과가_빈_리스트면_entry가_없는_map을_반환한다() {
    when(commentJpaRepository.findAllCommentsByPostId(POST_ID)).thenReturn(Collections.EMPTY_LIST);

    Map<Long, List<CommentOfPostDto>> commentsMap = getAllCommentsOfPostService.apply(POST_ID);

    assertThat(commentsMap).isEmpty();
  }

  @Test
  void 조회결과에_모두삭제된_그룹이_있으면_그룹을_삭제하고_반환한다() {

    when(commentJpaRepository.findAllCommentsByPostId(POST_ID)).thenReturn(createDtos());

    Map<Long, List<CommentOfPostDto>> commentsMap = getAllCommentsOfPostService.apply(POST_ID);

    commentsMap.entrySet().stream().forEach(entry -> {
      List<CommentOfPostDto> value = entry.getValue();
      for (CommentOfPostDto commentOfPostDto : value) {
        System.out.println(commentOfPostDto);
      }
    });

    assertThat(commentsMap).satisfies( map -> {
      assertThat(map).doesNotContainKey(5L);
      assertThat(map).doesNotContainKey(7L);
        }
    );
  }

  List<CommentOfPostDto> createDtos() {
    return List.of(
        new CommentOfPostDto(1L, "content", CommentStatus.CREATED, null, LocalDateTime.now(), 1L,
            "nickname"),
        new CommentOfPostDto(4L, "content", CommentStatus.DELETED, null, LocalDateTime.now(), 1L,
            "nickname"),
        new CommentOfPostDto(5L, "content", CommentStatus.DELETED, null, LocalDateTime.now(), 1L,
            "nickname"),
        new CommentOfPostDto(2L, "content", CommentStatus.CREATED, 1L, LocalDateTime.now(), 1L,
            "nickname"),
        new CommentOfPostDto(3L, "content", CommentStatus.CREATED, 1L, LocalDateTime.now(), 1L,
            "nickname"),
        new CommentOfPostDto(6L, "content", CommentStatus.CREATED, 4L, LocalDateTime.now(), 1L,
            "nickname"),
        new CommentOfPostDto(7L, "content", CommentStatus.DELETED, null, LocalDateTime.now(), 1L,
            "nickname"),
        new CommentOfPostDto(8L, "content", CommentStatus.DELETED, 5L, LocalDateTime.now(), 1L,
            "nickname")
    );
  }
}