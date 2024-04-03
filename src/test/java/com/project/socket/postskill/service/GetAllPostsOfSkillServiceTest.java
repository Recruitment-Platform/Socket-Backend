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
import com.project.socket.skill.exception.SkillNotFoundException;
import com.project.socket.skill.repository.SkillJpaRepository;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import java.time.LocalDateTime;
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
  SkillJpaRepository skillJpaRepository;

  @Mock
  PostSkillJpaRepository postSkillJpaRepository;

  private final List<String> hashTags = List.of("Java", "Spring");
  private final List<Long> skillIds = List.of(1L, 2L);

  private int page = 0;
  private int size = 3;

  Pageable pageable = PageRequest.of(page, size, Direction.DESC, "createdAt");
  OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.DESC, post.createdAt);
  Page<PostDto> expectedPage = new PageImpl<>(samplePostDtos(), pageable, samplePostDtos().size());


  @Test
  void skillName과_관련된_게시물이_있으면_성공적으로_조회한다() {
    String skillName = "Java,Spring";
    HashSet<Long> allPostIdBySkill = new HashSet<>(List.of(1L, 2L, 3L));

    // Stub
    when(skillJpaRepository.findAllIdBySkillNames(anyList())).thenReturn(skillIds);
    when(postSkillJpaRepository.findPostIdsBySkillIds(anyList())).thenReturn(allPostIdBySkill);
    when(postJpaRepository.getPostsByHashTag(any(), any(Pageable.class), any()))
        .thenReturn(expectedPage);

    Page<PostDto> actualPosts
        = getAllPostsOfSkillService.getPostsUsingSkill(skillName, pageable);

    assertThat(expectedPage).isEqualTo(actualPosts);
    verify(skillJpaRepository, times(1)).findAllIdBySkillNames(hashTags);
    verify(postSkillJpaRepository, times(1)).findPostIdsBySkillIds(skillIds);
    verify(postJpaRepository, times(1))
        .getPostsByHashTag(allPostIdBySkill, pageable, orderSpecifier);
  }


  @Test
  void skillName이_null_일_경우_모든_게시물을_페이징해서_조회한다() {
    String skillName = null;
    HashSet<Long> allPostIdBySkill = new HashSet<>();

    when(postJpaRepository.getPostsByHashTag(any(), any(Pageable.class), any()))
        .thenReturn(expectedPage);

    Page<PostDto> actualPosts
        = getAllPostsOfSkillService.getPostsUsingSkill(skillName, pageable);

    assertThat(expectedPage).isEqualTo(actualPosts);
    verify(skillJpaRepository, times(0)).findAllIdBySkillNames(hashTags);
    verify(postSkillJpaRepository, times(0)).findPostIdsBySkillIds(skillIds);
    verify(postJpaRepository, times(1)).getPostsByHashTag(allPostIdBySkill, pageable,
        orderSpecifier);
  }

  @Test
  void skillName이_빈_문자열_일경우_모든_게시물을_페이징해서_조회한다() {
    String skillName = "";
    HashSet<Long> allPostIdBySkill = new HashSet<>();
    Page<PostDto> expectedPage =
        new PageImpl<>(samplePostDtos(), pageable, samplePostDtos().size());

    when(postJpaRepository.getPostsByHashTag(any(), any(Pageable.class), any()))
        .thenReturn(expectedPage);

    Page<PostDto> actualPosts
        = getAllPostsOfSkillService.getPostsUsingSkill(skillName, pageable);

    assertThat(expectedPage).isEqualTo(actualPosts);
    verify(skillJpaRepository, times(0)).findAllIdBySkillNames(hashTags);
    verify(postSkillJpaRepository, times(0)).findPostIdsBySkillIds(skillIds);
    verify(postJpaRepository, times(1)).getPostsByHashTag(allPostIdBySkill, pageable,
        orderSpecifier);
  }

  @Test
  void skillName_조회결과가_없으면_SkillNotFoundException_예외가_발생한다() {
    String skillName = "Java";
    Pageable pageable = PageRequest.of(page, size, Direction.DESC, "createdAt");

    when(skillJpaRepository.findAllIdBySkillNames(anyList())).thenReturn(List.of());

    assertThatThrownBy(() -> getAllPostsOfSkillService.getPostsUsingSkill(skillName, pageable))
        .isInstanceOf(SkillNotFoundException.class);
  }

  @Test
  void order_정렬기준이_case에_없으면_UnsupportSortException_예외가_발생한다() {
    String skillName = "Java,Spring";
    HashSet<Long> postIds = new HashSet<>(List.of(1L, 2L, 3L));
    Pageable pageable = PageRequest.of(page, size, Direction.DESC, "wrongOrder");

    when(skillJpaRepository.findAllIdBySkillNames(anyList())).thenReturn(skillIds);
    when(postSkillJpaRepository.findPostIdsBySkillIds(anyList())).thenReturn(postIds);

    assertThatThrownBy(() -> getAllPostsOfSkillService.getPostsUsingSkill(skillName, pageable))
        .isInstanceOf(UnsupportedSortException.class);
  }


  List<PostDto> samplePostDtos() {
    return List.of(
        new PostDto(1L, "title1", "content1", PostType.STUDY, PostMeeting.ONLINE,
            PostStatus.CREATED, 1L, "nickname", LocalDateTime.now()),
        new PostDto(2L, "title2", "content2", PostType.STUDY, PostMeeting.OFFLINE,
            PostStatus.CREATED, 1L, "nickname", LocalDateTime.now()),
        new PostDto(3L, "title3", "content3", PostType.STUDY, PostMeeting.OFFLINE,
            PostStatus.CREATED, 1L, "nickname", LocalDateTime.now())

    );
  }

}

