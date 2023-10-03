package com.project.socket.user.repository;

import com.project.socket.user.model.SocialProvider;
import com.project.socket.user.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {

  Optional<User> findBySocialIdAndSocialProvider(String socialId, SocialProvider socialProvider);

  boolean existsByNickname(String nickname);
}
