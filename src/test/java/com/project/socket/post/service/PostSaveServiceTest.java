package com.project.socket.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.socket.post.model.Post;
import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostStatus;
import com.project.socket.post.model.PostType;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.post.service.usecase.PostSaveCommand;
import com.project.socket.postskill.model.PostSkill;
import com.project.socket.postskill.repository.PostSkillJpaRepository;
import com.project.socket.skill.model.Skill;
import com.project.socket.skill.repository.SkillJpaRepository;
import com.project.socket.user.exception.UserNotFoundException;
import com.project.socket.user.model.User;
import com.project.socket.user.repository.UserJpaRepository;
import java.util.Collections;
import java.util.List;
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

  @Mock
  SkillJpaRepository skillJpaRepository;

  @Mock
  PostSkillJpaRepository postSkillJpaRepository;

  @Captor
  ArgumentCaptor<Post> postCaptor;

  @Captor
  ArgumentCaptor<Skill> skillCaptor;

  @Captor
  ArgumentCaptor<PostSkill> postSkillCaptor;

  @Captor
  ArgumentCaptor<String> hashTagStringCaptor;

  PostSaveCommand createPost() {
    return new PostSaveCommand("테스트 제목", "테스트 내용", PostType.PROJECT, PostMeeting.ONLINE, 1L,
        List.of("Java"));
  }

  PostSaveCommand createPostWithNoTag() {
    return new PostSaveCommand("테스트 제목", "테스트 내용", PostType.PROJECT,
        PostMeeting.ONLINE, 1L, Collections.emptyList());
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
  void 유저가_존재하고_태그값이_없을경우_Post를_생성한다() {
    PostSaveCommand postNewWithNoTag = createPostWithNoTag();
    User writer = User.builder().userId(1L).build();

    Post post = Post.builder().id(1L)
        .title(postNewWithNoTag.title())
        .postContent(postNewWithNoTag.postContent())
        .postType(postNewWithNoTag.postType())
        .postMeeting(postNewWithNoTag.postMeeting())
        .build();

    when(userJpaRepository.findById(anyLong())).thenReturn(Optional.of(writer));
    when(postJpaRepository.save(any())).thenReturn(post);

    Post savedPost = postSaveService.createPost(postNewWithNoTag);

    verify(postJpaRepository).save(postCaptor.capture());
    Post capturePost = postCaptor.getValue();

    assertAll(
        () -> assertThat(savedPost.getId()).isEqualTo(1L),
        () -> assertThat(capturePost.getPostStatus()).isEqualTo(PostStatus.CREATED)
    );
  }

  @Test
  void 유저가_존재하고_입력된_태그값들이_Skill에_없을경우_생성후_PostSkill을_생성한다() {
    PostSaveCommand postNew = createPost();
    User writer = User.builder().userId(1L).build();

    Post post = Post.builder().id(1L)
        .title(postNew.title())
        .postContent(postNew.postContent())
        .postType(postNew.postType())
        .postMeeting(postNew.postMeeting())
        .build();

    Skill skill = Skill.builder().id(1L).skillName("Java").build();

    PostSkill postSkill = PostSkill.builder().id(1L).psPost(post).psSkill(skill).build();

    // Post stub
    when(userJpaRepository.findById(anyLong())).thenReturn(Optional.of(writer));
    when(postJpaRepository.save(any())).thenReturn(post);

    // Skill stub
    when(skillJpaRepository.findBySkillName(anyString())).thenReturn(
        Optional.empty());
    when(skillJpaRepository.save(any())).thenReturn(skill);

    // PostSkill stub
    when(postSkillJpaRepository.save(any())).thenReturn(postSkill);

    Post savedPost = postSaveService.createPost(postNew);

    // Skill verify
    verify(skillJpaRepository, times(1)).save(skillCaptor.capture());
    Skill captureSkill = skillCaptor.getValue();

    //PostSkill verify
    verify(postSkillJpaRepository, times(1)).save(postSkillCaptor.capture());
    PostSkill capturePostSkill = postSkillCaptor.getValue();

    assertAll(
        () -> assertThat(captureSkill.getSkillName()).isEqualTo("Java"),
        () -> assertThat(capturePostSkill.getPsPost().getId()).isEqualTo(1L),
        () -> assertThat(capturePostSkill.getPsSkill().getSkillName()).isEqualTo("Java")
    );
  }


  @Test
  void 유저가_존재하고_입력된_태그값들이_Skill에_있을경우_조회후_PostSkill을_생성한다() {
    PostSaveCommand postNew = createPost();
    User writer = User.builder().userId(1L).build();

    Post post = Post.builder().id(1L)
        .title(postNew.title())
        .postContent(postNew.postContent())
        .postType(postNew.postType())
        .postMeeting(postNew.postMeeting())
        .build();

    Skill skill = Skill.builder().id(1L).skillName("Java").build();

    PostSkill postSkill = PostSkill.builder().id(1L).psPost(post).psSkill(skill).build();

    // Post stub
    when(userJpaRepository.findById(anyLong())).thenReturn(Optional.of(writer));
    when(postJpaRepository.save(any())).thenReturn(post);

    // Skill stub
    when(skillJpaRepository.findBySkillName(anyString())).thenReturn(Optional.of(skill));

    // PostSkill stub
    when(postSkillJpaRepository.save(any())).thenReturn(postSkill);

    Post savedPost = postSaveService.createPost(postNew);

    // Skill verify
    verify(skillJpaRepository, times(1)).findBySkillName(hashTagStringCaptor.capture());
    String captureFindSkillName = hashTagStringCaptor.getValue();

    //PostSkill verify
    verify(postSkillJpaRepository, times(1)).save(postSkillCaptor.capture());
    PostSkill capturePostSkill = postSkillCaptor.getValue();

    assertAll(
        () -> assertThat(captureFindSkillName).isEqualTo("Java"),
        () -> assertThat(capturePostSkill.getPsPost().getId()).isEqualTo(1L),
        () -> assertThat(capturePostSkill.getPsSkill().getSkillName()).isEqualTo("Java")
    );
  }
}
