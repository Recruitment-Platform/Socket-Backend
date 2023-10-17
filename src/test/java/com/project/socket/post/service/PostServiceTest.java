package com.project.socket.post.service;

import com.project.socket.post.controller.dto.request.PostSaveRequestDto;
import com.project.socket.post.model.Post;
import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostStatus;
import com.project.socket.post.model.PostType;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.user.exception.UserNotFoundException;
import com.project.socket.user.model.User;
import com.project.socket.user.repository.UserJpaRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
class PostServiceTest {

    @InjectMocks
    PostService postService;

    @Mock
    PostJpaRepository postJpaRepository;

    @Mock
    UserJpaRepository userJpaRepository;

    @Captor
    ArgumentCaptor<Post> postCaptor;


    PostSaveRequestDto createPost() {
        return new PostSaveRequestDto(1L, new User("userA"), "테스트제목", "테스트 내용입니다",
                PostStatus.CREATED, PostType.PROJECT, PostMeeting.ONLINE);
    }

    @Test
    void id에_해당하는_유저가_없으면_UserNotFoundException_예외가_발생한다() {
        PostSaveRequestDto postNew = createPost();
        when(userJpaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertAll(
                () -> assertThatThrownBy(() -> postService.save(postNew)).isInstanceOf(UserNotFoundException.class),
                () -> verify(postJpaRepository, never()).findById(anyLong())
        );
    }

    @Test
    void 유저가_존재하면_Post를_생성한다() {
        PostSaveRequestDto postNew = createPost();
        User userNew = User.builder().userId(1L).build();

        Post post = Post.builder().id(1L)
                .user(userNew)
                .title(postNew.getTitle())
                .postContent(postNew.getPostContent())
                .postStatus(postNew.getPostStatus())
                .postType(postNew.getPostType())
                .postMeeting(postNew.getPostMeeting())
                .build();


        when(userJpaRepository.findById(anyLong())).thenReturn(Optional.of(userNew));
        when(postJpaRepository.findById(anyLong())).thenReturn(Optional.of(post));

        verify(postJpaRepository).save(postCaptor.capture());
        Post capturePost = postCaptor.getValue();

        assertThat(capturePost.getPostStatus()).isEqualTo(PostStatus.CREATED);
    }
}
