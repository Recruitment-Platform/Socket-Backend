package com.project.socket.security.oauth2.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class LoginSuccessResponseTest {

  @Test
  void 파라미터에_해당하는_LoginSuccessResponse_객체를_생성한다(){
    final boolean PROFILE_SETUP = false;
    final String ACCESS_TOKEN = "accessToken";
    final String REFRESH_TOKEN = "refreshToken";

    LoginSuccessResponse loginSuccessResponse = new LoginSuccessResponse(PROFILE_SETUP,
        ACCESS_TOKEN, REFRESH_TOKEN);

    assertAll(
        () -> assertThat(loginSuccessResponse.profileSetup()).isFalse(),
        () -> assertThat(loginSuccessResponse.accessToken()).isEqualTo(ACCESS_TOKEN),
        () -> assertThat(loginSuccessResponse.refreshToken()).isEqualTo(REFRESH_TOKEN)
    );
  }
}