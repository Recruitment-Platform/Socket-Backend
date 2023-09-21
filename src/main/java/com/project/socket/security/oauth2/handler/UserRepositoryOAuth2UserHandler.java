package com.project.socket.security.oauth2.handler;

import com.project.socket.user.model.User;
import com.project.socket.user.repository.UserJpaRepository;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepositoryOAuth2UserHandler implements Function<User, User> {

  private final UserJpaRepository userJpaRepository;

  @Override
  public User apply(User user) {
    return findUser(user).orElseGet(() -> saveUser(user));
  }

  private Optional<User> findUser(User user) {
    return userJpaRepository.findBySocialIdAndSocialProvider(user.getSocialId(),
        user.getSocialProvider());
  }

  private User saveUser(User user) {
    return userJpaRepository.save(user);
  }
}
