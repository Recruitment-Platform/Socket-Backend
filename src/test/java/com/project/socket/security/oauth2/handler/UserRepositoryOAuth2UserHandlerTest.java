package com.project.socket.security.oauth2.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.socket.user.model.User;
import com.project.socket.user.repository.UserJpaRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class UserRepositoryOAuth2UserHandlerTest {

  @InjectMocks
  UserRepositoryOAuth2UserHandler userRepositoryOAuth2UserHandler;

  @Mock
  UserJpaRepository userJpaRepository;

  @Test
  void 해당_유저가_가입되어있지_않으면_저장하고_반환한다(){
    User savedUser = User.builder().userId(1L).build();

    when(userJpaRepository.findBySocialIdAndSocialProvider(any(), any()))
        .thenReturn(Optional.empty());
    when(userJpaRepository.save(any())).thenReturn(savedUser);

    User user = userRepositoryOAuth2UserHandler.apply(User.builder().build());

    assertThat(user.getUserId()).isEqualTo(savedUser.getUserId());
  }

  @Test
  void 해당_유저가_가입되어있다면_찾은_유저를_반환한다(){
    User registeredUser = User.builder().userId(1L).build();

    when(userJpaRepository.findBySocialIdAndSocialProvider(any(), any()))
        .thenReturn(Optional.of(registeredUser));

    User user = userRepositoryOAuth2UserHandler.apply(User.builder().build());

    assertAll(
        () -> assertThat(user.getUserId()).isEqualTo(registeredUser.getUserId()),
        () -> verify(userJpaRepository, never()).save(any())
    );
  }
}