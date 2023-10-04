package com.project.socket.user.repository;

import static com.project.socket.user.model.SocialProvider.GOOGLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.project.socket.user.model.SocialProvider;
import com.project.socket.user.model.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test")
@DataJpaTest
@DisplayNameGeneration(ReplaceUnderscores.class)
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
}