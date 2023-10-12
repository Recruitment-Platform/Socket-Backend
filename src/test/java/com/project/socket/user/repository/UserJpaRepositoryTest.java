package com.project.socket.user.repository;

import static com.project.socket.user.model.SocialProvider.GOOGLE;
import static com.project.socket.user.model.SocialProvider.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.project.socket.common.annotation.CustomDataJpaTest;
import com.project.socket.config.JpaConfig;
import com.project.socket.user.model.Role;
import com.project.socket.user.model.SocialProvider;
import com.project.socket.user.model.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@CustomDataJpaTest
class UserJpaRepositoryTest {

  @Autowired
  UserJpaRepository userJpaRepository;

  @Test
  @Sql("findBySocialIdAndSocialProvider.sql")
  void 조건에_해당하는_유저가_있다면_유저가_담긴_Optinal을_반환한다() {
    final String SOCIAL_ID = "1234";
    final SocialProvider SOCIAL_PROVIDER = GOOGLE;

    Optional<User> optionalUser = userJpaRepository.findBySocialIdAndSocialProvider(
        SOCIAL_ID, SOCIAL_PROVIDER);

    assertThat(optionalUser).hasValueSatisfying(user -> {
      assertAll(
          () -> assertThat(user.getSocialId()).isEqualTo(SOCIAL_ID),
          () -> assertThat(user.getSocialProvider()).isEqualTo(GOOGLE)
      );
    });
  }

  @Test
  @Sql("findBySocialIdAndSocialProvider.sql")
  void 조건에_해당하는_유저가_없다면_빈_Optinal을_반환한다() {
    final String SOCIAL_ID = "1234";
    final SocialProvider SOCIAL_PROVIDER = SocialProvider.KAKAO;

    Optional<User> optionalUser = userJpaRepository.findBySocialIdAndSocialProvider(
        SOCIAL_ID, SOCIAL_PROVIDER);

    assertThat(optionalUser).isEmpty();
  }

  @Test
  @Sql("existsByNicknameTest.sql")
  void nickname에_해당하는_유저가_있다면_true를_반환한다() {
    boolean result = userJpaRepository.existsByNickname("nickname");
    assertThat(result).isTrue();
  }

  @Test
  void nickname에_해당하는_유저가_없다면_false를_반환한다() {
    boolean result = userJpaRepository.existsByNickname("nickname");
    assertThat(result).isFalse();
  }

  @Test
  @Sql("findById.sql")
  void id에_해당하는_유저가_있다면_유저가_담긴_Optional_객체를_반환한다() {
    Optional<User> userOptional = userJpaRepository.findById(1L);
    assertThat(userOptional).isPresent();
  }

  @Test
  void id에_해당하는_유저가_없으면_빈_Optional_객체를_반환한다() {
    Optional<User> userOptional = userJpaRepository.findById(1L);
    assertThat(userOptional).isNotPresent();
  }

  @Test
  void User_엔티티를_저장한다() {
    User user = User.builder().role(Role.ROLE_USER).socialProvider(KAKAO).socialId("123")
                    .build();
    User savedUser = userJpaRepository.save(user);
    assertThat(savedUser.getUserId()).isNotNull();
  }
}