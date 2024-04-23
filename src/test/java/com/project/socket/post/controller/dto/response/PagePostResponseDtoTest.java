package com.project.socket.post.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostStatus;
import com.project.socket.post.model.PostType;
import com.project.socket.post.service.usecase.PostDto;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
class PagePostResponseDtoTest {

  @Test
  void 빈_페이지일_경우_빈_리스트와_값이없는_페이징_데이터를_반환한다() {

    Page<PostDto> postDtoPage = new PageImpl<>(Collections.emptyList());

    PagePostResponseDto result = PagePostResponseDto.toResponse(postDtoPage);

    assertAll(
        () -> assertThat(result.content()).isEmpty(),
        () -> assertThat(result.pageMetaData().totalElements()).isEqualTo(0),
        () -> assertThat(result.pageMetaData().sortCriteria()).isEqualTo("")
    );
  }

  @Test
  void 빈_페이지가_아닐경우_게시물과_페이징_데이터를_반환한다() {
    Pageable pageable = PageRequest.of(0, 3, Sort.Direction.DESC, "createdAt");

    Page<PostDto> postDtoPage = new PageImpl<>(postDtoList(), pageable,
        postDtoList().size());

    PagePostResponseDto result = PagePostResponseDto.toResponse(postDtoPage);

    assertAll(
        () -> assertThat(result.content()).isNotEmpty(),
        () -> assertThat(result.pageMetaData().totalElements()).isEqualTo(5),
        () -> assertThat(result.pageMetaData().totalPages()).isEqualTo(2),
        () -> assertThat(result.pageMetaData().sortCriteria()).isEqualTo("createdAt")
    );
  }

  List<PostDto> postDtoList() {
    return List.of(
        new PostDto(1L, "title1", "content1", PostType.STUDY, PostMeeting.ONLINE,
            PostStatus.CREATED, 1L, "nickname", LocalDateTime.now()),
        new PostDto(2L, "title2", "content2", PostType.PROJECT, PostMeeting.OFFLINE,
            PostStatus.CREATED, 1L, "nickname", LocalDateTime.now()),
        new PostDto(3L, "title3", "content3", PostType.STUDY, PostMeeting.ON_OFFLINE,
            PostStatus.CREATED, 1L, "nickname", LocalDateTime.now()),
        new PostDto(4L, "title4", "content4", PostType.PROJECT, PostMeeting.ONLINE,
            PostStatus.MODIFIED, 1L, "nickname", LocalDateTime.now()),
        new PostDto(5L, "title5", "content5", PostType.STUDY, PostMeeting.OFFLINE,
            PostStatus.CREATED, 1L, "nickname", LocalDateTime.now())

    );
  }
}