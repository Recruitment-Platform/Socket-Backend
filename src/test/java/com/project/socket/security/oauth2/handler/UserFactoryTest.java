package com.project.socket.security.oauth2.handler;

import static com.project.socket.user.model.SocialProvider.GITHUB;
import static com.project.socket.user.model.SocialProvider.GOOGLE;
import static com.project.socket.user.model.SocialProvider.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.project.socket.user.model.User;
import java.util.Map;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class UserFactoryTest {

  private final String SOCIAL_ID = "123";
  private final String EMAIL = "test@google.com";

  @Test
  void kakao_attribute에_해당하는_유저를_반환한다() {
    String givenProvider = "kakao";
    Map<String, Object> givenAttribute = getKakaoAttribute();

    User user = UserFactory.of(givenProvider, givenAttribute);

    assertAll(
        () -> assertThat(user.getSocialId()).isEqualTo(SOCIAL_ID),
        () -> assertThat(user.getEmail()).isEqualTo(EMAIL),
        () -> assertThat(user.getSocialProvider()).isEqualTo(KAKAO)
    );
  }

  @Test
  void github_attribute에_해당하는_유저를_반환한다() {
    String givenProvider = "github";
    Map<String, Object> givenAttribute = getGithubAttribute();

    User user = UserFactory.of(givenProvider, givenAttribute);

    assertAll(
        () -> assertThat(user.getSocialId()).isEqualTo(SOCIAL_ID),
        () -> assertThat(user.getEmail()).isEqualTo(EMAIL),
        () -> assertThat(user.getSocialProvider()).isEqualTo(GITHUB)
    );
  }

  @Test
  void email이_없는_github_attribute에_해당하는_유저를_반환한다() {
    String givenProvider = "github";
    Map<String, Object> givenAttribute = getEmptyEmailGithubAttribute();

    User user = UserFactory.of(givenProvider, givenAttribute);

    assertAll(
        () -> assertThat(user.getSocialId()).isEqualTo(SOCIAL_ID),
        () -> assertThat(user.getEmail()).isNull(),
        () -> assertThat(user.getSocialProvider()).isEqualTo(GITHUB)
    );
  }

  @Test
  void google_attribute에_해당하는_유저를_반환한다() {
    String givenProvider = "google";
    Map<String, Object> givenAttribute = getGoogleAttribute();

    User user = UserFactory.of(givenProvider, givenAttribute);

    assertAll(
        () -> assertThat(user.getSocialId()).isEqualTo(SOCIAL_ID),
        () -> assertThat(user.getEmail()).isEqualTo(EMAIL),
        () -> assertThat(user.getSocialProvider()).isEqualTo(GOOGLE)
    );
  }

  @Test
  void 지원하지않는_provider라면_IllegalArgumentException_예외가_발생한다() {
    String givenProvider = "naver";

    assertThatThrownBy(() -> UserFactory.of(givenProvider, Map.of()))
        .isInstanceOf(IllegalArgumentException.class);
  }

  private Map<String, Object> getKakaoAttribute() {
    Map<String, String> kakaoAccount = Map.of("email", EMAIL);
    return Map.of("kakao_account", kakaoAccount, "id", SOCIAL_ID);
  }

  private Map<String, Object> getGoogleAttribute() {
    return Map.of("email", EMAIL, "sub", SOCIAL_ID);
  }

  private Map<String, Object> getGithubAttribute() {
    return Map.of("email", EMAIL, "id", SOCIAL_ID);
  }

  private Map<String, Object> getEmptyEmailGithubAttribute() {
    return Map.of("id", SOCIAL_ID);
  }
}