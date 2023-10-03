package com.project.socket.user.service;

import com.project.socket.user.exception.DuplicatedNicknameException;
import com.project.socket.user.exception.UserNotFoundException;
import com.project.socket.user.model.User;
import com.project.socket.user.repository.UserJpaRepository;
import com.project.socket.user.service.usecase.SignupUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SignupService implements SignupUseCase {

  private final UserJpaRepository userJpaRepository;

  @Override
  @Transactional
  public User signup(SignupCommand signupCommand) {
    checkDuplicatedNickname(signupCommand.nickname());
    User userForUpdate = findUser(signupCommand.userId());
    userForUpdate.updateAdditionalInfo(signupCommand.nickname(), signupCommand.githubLink());
    return userForUpdate;
  }

  private void checkDuplicatedNickname(String nickname) {
    boolean isDuplicatedNickname = userJpaRepository.existsByNickname(nickname);
    if (isDuplicatedNickname) {
      throw new DuplicatedNicknameException();
    }
  }

  private User findUser(Long userId) {
    return userJpaRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
  }
}
