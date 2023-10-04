package com.project.socket.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.socket.user.exception.DuplicatedNicknameException;
import com.project.socket.user.exception.UserNotFoundException;
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
class SignupServiceTest {

  @InjectMocks
  SignupService signupService;

  @Mock
  UserJpaRepository userJpaRepository;

  @Test
  void nickname이_중복되면_DuplicatedNicknameException_예외가_발생한다() {
    SignupCommand command = createCommand();
    when(userJpaRepository.existsByNickname(anyString())).thenReturn(true);

    assertAll(
        () -> assertThatThrownBy(() -> signupService.signup(command))
            .isInstanceOf(DuplicatedNicknameException.class),
        () -> verify(userJpaRepository, never()).findById(any())
    );
  }

  @Test
  void id에_해당하는_유저가_존재하지_않으면_UserNotFoundException_예외가_발생한다() {
    SignupCommand command = createCommand();
    when(userJpaRepository.existsByNickname(anyString())).thenReturn(false);
    when(userJpaRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertAll(
        () -> assertThatThrownBy(() -> signupService.signup(command))
            .isInstanceOf(UserNotFoundException.class)
    );
  }

  @Test
  void 성공적으로_정보를_업데이트하고_유저를_반환한다() {
    SignupCommand command = createCommand();
    User givenUser = User.builder().userId(command.userId()).build();
    when(userJpaRepository.existsByNickname(anyString())).thenReturn(false);
    when(userJpaRepository.findById(anyLong())).thenReturn(Optional.of(givenUser));

    User updatedUser = signupService.signup(command);
    assertAll(
        () -> assertThat(updatedUser.getGithubLink()).isEqualTo(command.githubLink()),
        () -> assertThat(updatedUser.getNickname()).isEqualTo(command.nickname())
    );
  }

  SignupCommand createCommand() {
    return new SignupCommand(1L, "nickname", "githubLink");
  }
}