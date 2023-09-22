package com.project.socket.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.project.socket.security.exception.InvalidJwtException;
import com.project.socket.user.model.Role;
import com.project.socket.user.model.User;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class JwtProviderTest {

  JwtProvider jwtProvider = new JwtProvider("jwt-secret-key-for-test-profile-test");

  @Test
  void 유저_정보에_해당하는_access_token을_생성하고_반환한다() {
    User user = User.builder().userId(1L).role(Role.ROLE_USER).build();

    String accessToken = jwtProvider.createAccessToken(user);

    assertThat(accessToken).isNotNull();
  }

  @Test
  void 유저_정보에_해당하는_refresh_token을_생성하고_반환한다() {
    User user = User.builder().userId(1L).role(Role.ROLE_USER).build();

    String accessToken = jwtProvider.createRefreshToken(user);

    assertThat(accessToken).isNotNull();
  }

  @Test
  void 토큰이_null이면_InvalidJwtExeption_예외가_발생한다() {
    assertThatThrownBy(() -> jwtProvider.validateToken(null))
        .isInstanceOf(InvalidJwtException.class)
        .hasMessage("JWT String argument cannot be null or empty.");
  }

  @Test
  void 토큰이_빈_문자열이면_InvalidJwtExeption_예외가_발생한다() {
    assertThatThrownBy(() -> jwtProvider.validateToken(""))
        .isInstanceOf(InvalidJwtException.class)
        .hasMessage("JWT String argument cannot be null or empty.");
  }

  @Test
  void 토큰이_유효하지_않으면_InvalidJwtExeption_예외가_발생한다() {
    assertThatThrownBy(() -> jwtProvider.validateToken("invalid.token.value"))
        .isInstanceOf(InvalidJwtException.class);
  }

  @Test
  void 유효한_토큰을_검증한다() {
    User user = User.builder().userId(1L).role(Role.ROLE_USER).build();
    String accessToken = jwtProvider.createAccessToken(user);

    jwtProvider.validateToken(accessToken);
  }
}