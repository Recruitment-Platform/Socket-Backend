package com.project.socket.postskill.service;

import static com.project.socket.post.model.QPost.post;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.socket.post.exception.UnsupportedSortException;
import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostStatus;
import com.project.socket.post.model.PostType;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.post.service.usecase.PostDto;
import com.project.socket.postskill.repository.PostSkillJpaRepository;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
class GetAllPostsOfSkillServiceTest {

  @InjectMocks
  GetAllPostsOfSkillService getAllPostsOfSkillService;

  @Mock
  PostJpaRepository postJpaRepository;

  @Mock
  PostSkillJpaRepository postSkillJpaRepository;

  private final List<Long> hashTagIds = List.of(1L, 2L);

  Pageable pageable = PageRequest.of(0, 3, Direction.DESC, "createdAt");
  OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.DESC, post.createdAt);
  Page<PostDto> expectedPage = new PageImpl<>(samplePostDtos(), pageable, samplePostDtos().size());


  @Test
  void hashTagIds와_연관있는_게시물이_있으면_해당_게시물을_조회한다() {
    // given
    HashSet<Long> allPostIdBySkill = new HashSet<>(List.of(1L, 2L, 3L));

    // when
    when(postSkillJpaRepository.findPostIdsBySkillIds(anyList())).thenReturn(allPostIdBySkill);
    when(postJpaRepository.getPostsByHashTag(any(), any(Pageable.class), any()))
        .thenReturn(expectedPage);

    // then
    Page<PostDto> actualPosts
        = getAllPostsOfSkillService.getPostsUsingSkill(hashTagIds, pageable);

    assertThat(expectedPage).isEqualTo(actualPosts);
    verify(postSkillJpaRepository, times(1)).findPostIdsBySkillIds(hashTagIds);
    verify(postJpaRepository, times(1))
        .getPostsByHashTag(allPostIdBySkill, pageable, orderSpecifier);
  }

  @Test
  void hashTagIds와_연관있는_게시물이_존재하지_않을경우_빈_페이지를_반환한다() {
    HashSet<Long> allPostIdBySkill = new HashSet<>();

    when(postSkillJpaRepository.findPostIdsBySkillIds(anyList())).thenReturn(allPostIdBySkill);

    Page<PostDto> actualPosts = getAllPostsOfSkillService.getPostsUsingSkill(hashTagIds,
        pageable);

    assertThat(actualPosts).isEmpty();
  }

  @Test
  void hashTagIds가_빈_문자열_일경우_모든_게시물을_페이징해서_조회한다() {
    List<Long> hashTagIds = Collections.emptyList();
    HashSet<Long> allPostIdBySkill = new HashSet<>();

    when(postJpaRepository.getPostsByHashTag(any(), any(Pageable.class), any()))
        .thenReturn(expectedPage);

    Page<PostDto> actualPosts
        = getAllPostsOfSkillService.getPostsUsingSkill(hashTagIds, pageable);

    assertThat(expectedPage).isEqualTo(actualPosts);
    verify(postSkillJpaRepository, times(0)).findPostIdsBySkillIds(hashTagIds);
    verify(postJpaRepository, times(1)).getPostsByHashTag(allPostIdBySkill, pageable,
        orderSpecifier);
  }

  @Test
  void hashTagIds가_null_일_경우_모든_게시물을_페이징해서_조회한다() {
    HashSet<Long> allPostIdBySkill = new HashSet<>();

    when(postJpaRepository.getPostsByHashTag(any(), any(Pageable.class), any()))
        .thenReturn(expectedPage);

    Page<PostDto> actualPosts
        = getAllPostsOfSkillService.getPostsUsingSkill(null, pageable);

    assertThat(expectedPage).isEqualTo(actualPosts);
    verify(postSkillJpaRepository, times(0)).findPostIdsBySkillIds(hashTagIds);
    verify(postJpaRepository, times(1)).getPostsByHashTag(allPostIdBySkill, pageable,
        orderSpecifier);
  }


  @Test
  void order_정렬기준이_case에_없으면_UnsupportSortException_예외가_발생한다() {

    HashSet<Long> allPostIdBySkill = new HashSet<>(List.of(1L, 2L, 3L));
    Pageable pageable = PageRequest.of(0, 3, Direction.DESC, "wrongOrder");

    when(postSkillJpaRepository.findPostIdsBySkillIds(anyList())).thenReturn(allPostIdBySkill);

    assertThatThrownBy(() -> getAllPostsOfSkillService.getPostsUsingSkill(hashTagIds, pageable))
        .isInstanceOf(UnsupportedSortException.class);
  }


  List<PostDto> samplePostDtos() {
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

